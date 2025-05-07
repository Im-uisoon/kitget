<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import Header from '../Header.vue'

// Route and Router setup
const route = useRoute()
const router = useRouter()

// Reactive state
const post = ref({})
const editedPost = ref({})
const isLoading = ref(true)
const isEditing = ref(false)
const isAuthenticated = ref(false)
const isAdmin = ref(false)

const apiBaseUrl = 'http://localhost:8080/api'

// 세션 확인
const checkSession = async () => {
  try {
    const response = await axios.get(`${apiBaseUrl}/session-check`, {
      withCredentials: true,
    })
    isAuthenticated.value = response.data.isAuthenticated
    isAdmin.value = response.data.authorities?.includes('ROLE_ADMIN')
  } catch (error) {
    console.error('Session check failed:', error)
    isAuthenticated.value = false
    isAdmin.value = false
  }
}

// 글 불러오기
const fetchPost = async () => {
  try {
    isLoading.value = true
    const response = await axios.get(`${apiBaseUrl}/content/${route.params.page}`, {
      withCredentials: true,
    })
    post.value = response.data
    editedPost.value = { ...response.data }
  } catch (error) {
    console.error('Error fetching post:', error)
    if (error.response?.status === 404) {
      alert('게시물을 찾을 수 없습니다.')
    } else {
      alert('게시물을 불러오는 데 실패했습니다.')
    }
  } finally {
    isLoading.value = false
  }
}

// 글 수정 버튼
const toggleEdit = () => {
  if (!isAuthenticated.value) {
    router.push('/login')
    return
  }
  if (post.value.author !== getCurrentUsername() && !isAdmin.value) {
    alert('수정 권한이 없습니다.')
    return
  }
  isEditing.value = !isEditing.value
}

const getCurrentUsername = () => {
  return 'currentUser'
}

// 글 수정
const saveEdit = async () => {
  try {
    const response = await axios.put(`${apiBaseUrl}/content/${route.params.page}`, editedPost.value, { withCredentials: true })
    post.value = response.data
    isEditing.value = false
    alert('게시물이 수정되었습니다.')
  } catch (error) {
    console.error('Error updating post:', error)
    if (error.response?.status === 403) {
      alert('수정 권한이 없습니다.')
    } else if (error.response?.status === 401) {
      router.push('/login')
    } else {
      alert('게시물 수정에 실패했습니다.')
    }
  }
}

// 글 삭제
const deletePost = async () => {
  if (!isAuthenticated.value) {
    router.push('/login')
    return
  }
  if (post.value.author !== getCurrentUsername() && !isAdmin.value) {
    alert('삭제 권한이 없습니다.')
    return
  }
  if (!confirm('정말 삭제하시겠습니까?')) return
  try {
    await axios.delete(`${apiBaseUrl}/content/${route.params.page}`, {
      withCredentials: true,
    })
    alert('게시물이 삭제되었습니다.')
    router.push(`/${post.value.postFrom}/1`)
  } catch (error) {
    console.error('Error deleting post:', error)
    if (error.response?.status === 403) {
      alert('삭제 권한이 없습니다.')
    } else if (error.response?.status === 401) {
      router.push('/login')
    } else {
      alert('게시물 삭제에 실패했습니다.')
    }
  }
}

onMounted(async () => {
  await checkSession()
  await fetchPost()
})
</script>

<template>
  <Header />
  <div class="flex items-center justify-center px-100">
    <section class="w-full h-full">
      <h1 class="text-2xl text-black mb-2 mt-14">게시글</h1>
      <hr />
      <div v-if="isLoading" class="mt-10">로딩 중...</div>
      <div v-else-if="Object.keys(post).length === 0" class="mt-10">게시물을 찾을 수 없습니다.</div>
      <div v-else>
        <div v-if="isEditing" class="mt-10">
          <input v-model="editedPost.title" class="w-full p-2 mb-2 text-xl text-black bg-gray-100 rounded" placeholder="제목을 입력하세요" />
        </div>
        <h1 v-else class="text-2xl text-black mb-2 mt-10 title-clamp">
          {{ post.title }}
        </h1>
        <div class="w-full h-60 mb-3 overflow-y-auto">
          <textarea v-if="isEditing" v-model="editedPost.content" class="w-full h-full p-2 text-black bg-gray-100 rounded" placeholder="내용을 입력하세요" />
          <p v-else class="text-black mb-2 mt-5 whitespace-pre-wrap">
            {{ post.content }}
          </p>
        </div>
        <div class="flex justify-between">
          <router-link :to="`/${post.postFrom}/1`" class="my-2 p-1 rounded-lg ring-2"> 뒤로가기 </router-link>
          <div class="my-2 flex gap-3">
            <button v-if="isEditing" @click="saveEdit" class="p-1 rounded-lg bg-green-600 text-white">저장</button>
            <button v-else @click="toggleEdit" class="p-1 rounded-lg bg-blue-600 text-white">수정</button>
            <button @click="deletePost" class="p-1 rounded-lg bg-red-600 text-white">삭제</button>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.overflow-y-auto {
  scrollbar-width: thin;
  scrollbar-color: #888 #f1f1f1;
}

.overflow-y-auto::-webkit-scrollbar {
  width: 8px;
}

.overflow-y-auto::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 4px;
}

.overflow-y-auto::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.title-clamp {
  word-break: break-word;
  overflow-wrap: break-word;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
