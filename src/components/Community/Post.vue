<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Header from '@/components/Header.vue'

const router = useRouter()

const title = ref('')
const content = ref('')
const postFrom = ref('resell')
const isAdmin = ref(false)

const checkAdminStatus = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/session-check', {
      credentials: 'include',
    })
    if (response.ok) {
      const data = await response.json()
      isAdmin.value = data.authorities?.includes('ROLE_ADMIN') || false
    }
  } catch (error) {
    console.error('관리자 확인 오류:', error)
  }
}

const submitPost = async () => {
  try {
    const postData = { title: title.value, content: content.value, postFrom: postFrom.value }
    const response = await fetch('http://localhost:8080/api/post', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(postData),
      credentials: 'include',
    })
    if (response.ok) {
      alert('게시글 작성 완료!')
      router.push(`/${postFrom.value}/1`)
    } else {
      const errorData = await response.json()
      alert(`작성 실패: ${errorData.message || '오류 발생'}`)
    }
  } catch (error) {
    console.error('게시글 제출 오류:', error)
    alert('서버 오류가 발생했습니다.')
  }
}

onMounted(() => checkAdminStatus())
</script>

<template>
  <Header />
  <div class="flex items-center justify-center px-100">
    <section class="w-full h-full">
      <h1 class="text-2xl text-black mb-2 mt-14">게시글 작성</h1>
      <hr />
      <form @submit.prevent="submitPost" class="mt-2">
        <select v-model="postFrom" class="inputBox2">
          <option value="resell">중고거래</option>
          <option value="review">구매후기</option>
          <option v-if="isAdmin" value="faq">FAQ</option>
          <option v-if="isAdmin" value="notice">공지사항</option>
        </select>
        <div>
          <input v-model="title" type="text" class="inputBox2" placeholder="제목을 입력해 주세요." required />
          <textarea v-model="content" class="inputBox2 h-40 sm:h-72" placeholder="글을 작성해 주세요..." required></textarea>
        </div>
        <div class="flex justify-end">
          <button class="loginBtn" type="submit">작성 완료</button>
        </div>
      </form>
    </section>
  </div>
</template>

<style scoped>
@import 'tailwindcss';

.inputBox2 {
  @apply mt-5 p-1 w-full rounded-md ring-1 hover:ring-blue-400;
}

.loginBtn {
  @apply mt-5 p-2 ring-1 rounded-lg bg-blue-600 dark:bg-blue-700 hover:bg-blue-700 dark:hover:bg-blue-600 w-full sm:w-32 text-white;
}
</style>
