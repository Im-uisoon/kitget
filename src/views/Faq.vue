<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'

const router = useRouter()
const route = useRoute()

const posts = ref([])
const totalPages = ref(0)
const currentPage = ref(1)
const sortOrder = ref('desc')
const isLoggedIn = ref(false)
const isAdmin = ref(false)

const fetchPosts = async (page, sort) => {
  try {
    const response = await axios.get(`http://localhost:8080/api/faq/${page}`, {
      params: { sort },
      withCredentials: true,
    })
    posts.value = response.data.posts
    totalPages.value = response.data.totalPages
    currentPage.value = response.data.currentPage
  } catch (error) {
    console.error('게시물 조회 오류:', error)
  }
}

const toggleSort = () => {
  sortOrder.value = sortOrder.value === 'desc' ? 'asc' : 'desc'
  fetchPosts(currentPage.value, sortOrder.value)
}

const goToPage = (page) => {
  router.push(`/faq/${page}`)
  fetchPosts(page, sortOrder.value)
}

const checkLoginStatus = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/session-check', {
      credentials: 'include',
    })
    if (response.status === 401) {
      isLoggedIn.value = false
      isAdmin.value = false
      return { isAuthenticated: false, isAdmin: false }
    } else {
      const data = await response.json()
      isLoggedIn.value = data.isAuthenticated
      isAdmin.value = data.authorities?.includes('ROLE_ADMIN') || false
      return { isAuthenticated: data.isAuthenticated, isAdmin: isAdmin.value }
    }
  } catch (error) {
    console.error('세션 확인 오류:', error)
    isLoggedIn.value = false
    isAdmin.value = false
    return { isAuthenticated: false, isAdmin: false }
  }
}

const redirectToPost = async () => {
  const { isAuthenticated, isAdmin } = await checkLoginStatus()
  if (!isAuthenticated) {
    router.push('/login')
  } else if (isAdmin) {
    router.push('/admin/post')
  } else {
    router.push('/post')
  }
}

onMounted(async () => {
  const page = parseInt(route.params.page) || 1
  await checkLoginStatus()
  fetchPosts(page, sortOrder.value)
})
</script>

<template>
  <Header />
  <!-- 게시물 박스 -->
  <div class="flex items-center justify-center px-100">
    <section class="w-full h-full">
      <!-- 1. 게시물 글 -->
      <h1 class="text-2xl text-black mb-1 mt-14">FAQ</h1>
      <p class="mb-1 text-gray-500">자주 묻는 질문들입니다</p>
      <hr />

      <!-- 2. 상단 메뉴 -->
      <div class="flex justify-between py-3">
        <div class="flex gap-3">
          <!-- 2.1. 글 작성 버튼 -->
          <button @click="redirectToPost" class="px-3 py-1 bg-blue-500 text-white rounded-md flex items-center hover:bg-blue-600 text-sm">
            <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
            </svg>
            글 작성
          </button>
          <!-- 2.2. 정렬 토글 버튼 -->
          <button @click="toggleSort" class="px-3 py-1 bg-gray-700 text-white rounded-md hover:bg-gray-600">
            <svg v-if="sortOrder === 'desc'" class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4h13M3 8h9M3 12h6m-6 8h18M3 16h3" />
            </svg>
            <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 20h13M3 16h9M3 12h6m-6-8h18M3 8h3" />
            </svg>
          </button>
        </div>

        <!-- 2.3. 페이지 정보 -->
        <span class="px-3 py-1 text-sm text-zinc-500 flex items-center"> {{ currentPage }} / {{ totalPages }} 페이지 </span>
      </div>

      <hr />

      <!-- 3. 게시물 목록 -->
      <ul class="divide-y">
        <li v-for="post in posts" :key="post.postId" class="py-3 hover:bg-blue-200 cursor-pointer" @click="router.push(`/content/${post.postId}`)">
          <div class="flex justify-between items-center">
            <div class="flex flex-col">
              <!-- 3.1. 게시물 제목 -->
              <span class="font-bold text-black hover:text-blue-600 line-clamp-1">
                {{ post.title }}
              </span>
              <!-- 3.2. 게시자 -->
              <span class="text-sm text-gray-500"> by {{ post.author }} </span>
            </div>
            <!-- 3.3. 게시 날짜 -->
            <span class="text-sm text-gray-500">
              {{ new Date(post.createdAt).toLocaleString() }}
            </span>
          </div>
        </li>
      </ul>

      <!-- 4. 페이지네이션 하단 -->
      <hr class="mb-4" />
      <div class="flex justify-between mb-10">
        <button v-if="currentPage > 1" @click="goToPage(currentPage - 1)" class="text-gray-500 hover:text-blue-600 flex items-center">
          <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
          </svg>
          Previous
        </button>
        <button v-else disabled class="text-gray-300 cursor-default flex items-center">
          <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
          </svg>
          Previous
        </button>
        <button v-if="currentPage < totalPages" @click="goToPage(currentPage + 1)" class="text-gray-500 hover:text-blue-600 flex items-center">
          Next
          <svg class="w-5 h-5 ml-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
          </svg>
        </button>
        <button v-else disabled class="text-gray-300 cursor-default flex items-center">
          Next
          <svg class="w-5 h-5 ml-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
          </svg>
        </button>
      </div>
    </section>
  </div>

  <Footer />
</template>

<style scoped></style>
