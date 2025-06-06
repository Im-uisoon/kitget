<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import Header from '../view/Header.vue'

const router = useRouter()
const notices = ref([])
const page = ref(0)
const totalPages = ref(1)
const userAuth = ref(null)
const userEmail = ref(null)

// 내 정보 불러오기
const fetchMyInfo = async () => {
  try {
    const res = await axios.get('tender-hally-imuisoon-0eaa6cd0.koyeb.app/api/me', { withCredentials: true })
    userAuth.value = res.data.auth
    userEmail.value = res.data.email
  } catch (e) {
    userAuth.value = null
    userEmail.value = null
  }
}

const fetchNotices = async () => {
  const res = await axios.get(`tender-hally-imuisoon-0eaa6cd0.koyeb.app/api/notice?page=${page.value}`, { withCredentials: true })
  notices.value = res.data.content
  totalPages.value = res.data.totalPages
}

const goToPage = (p) => {
  page.value = p
  fetchNotices()
}

const pagesToShow = computed(() => {
  const total = totalPages.value
  const current = page.value
  const max = 5
  let start = Math.max(0, current - 2)
  let end = Math.min(total, start + max)
  if (end - start < max) start = Math.max(0, end - max)
  return Array.from({ length: end - start }, (_, i) => start + i)
})

onMounted(async () => {
  await fetchMyInfo()
  await fetchNotices()
})
</script>

<template>
  <Header />
  <section class="pt-10 md:pt-20 mx-10 md:mx-25">
    <div>
      <div class="flex justify-between items-center mb-6">
        <h1 class="text-2xl font-bold text-gray-800 xl:text-3xl">공지사항</h1>
        <button v-if="userAuth === 'teacher'" @click="$router.push('/notice/write')" class="bg-blue-500 text-white px-4 py-2 xl:px-6 xl:py-4 rounded hover:bg-blue-600 transition">글쓰기</button>
      </div>
      <ul class="divide-y divide-gray-200">
        <li v-for="notice in notices" :key="notice.id" class="py-2 xl:py-15 flex justify-between items-center hover:bg-blue-50 transition rounded cursor-pointer" @click="$router.push(`/notice/${notice.id}`)">
          <div>
            <div class="font-semibold text-lg xl:text-xl text-gray-900">{{ notice.title }}</div>
            <div class="text-sm text-gray-500 xl:text-lg xl:pt-2">작성자: {{ notice.writerName }}</div>
          </div>
          <div class="text-xs text-gray-400 xl:text-lg">{{ new Date(notice.createdAt).toLocaleDateString() }}</div>
        </li>
      </ul>
      <div class="flex justify-center mt-6 gap-2 xl:gap-5">
        <button v-for="p in pagesToShow" :key="p" @click="goToPage(p)" :class="['px-3 py-1 xl:px-5 xl:py-3 rounded', page === p ? 'bg-blue-600 text-white font-bold' : 'bg-gray-200 text-gray-600 hover:bg-blue-100']">
          {{ p + 1 }}
        </button>
      </div>
    </div>
  </section>
</template>
