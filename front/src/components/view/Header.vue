<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const login = ref(true)
const email = ref(null)

const checkAuth = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/check', { withCredentials: true })
    login.value = response.data.isAuthenticated
    email.value = response.data.email
    if (!login.value) {
      router.push('/')
    }
  } catch (error) {
    console.error('인증 확인 에러:', error.response?.status, error.response?.data, error.message)
    login.value = false
    router.push('/')
  }
}

const logout = async () => {
  try {
    await axios.post('http://localhost:8080/api/logout', {}, { withCredentials: true })
    login.value = false
    email.value = null
    router.push('/')
  } catch (error) {
    console.error('로그아웃 에러:', error.response?.status, error.response?.data, error.message)
  }
}

onMounted(checkAuth)
</script>

<template>
  <section class="bg-blue-400 py-4 px-10 flex justify-between">
    <!-- 1.1. 로고 -->
    <router-link to="/about" class="font-[LogoFont] text-2xl text-white select-none">KitGet</router-link>
    <!-- 1.2. 로그인/로그아웃 -->
    <nav class="mt-1 text-white">
      <div v-if="!login">
        <router-link to="/"><i class="fa-solid fa-arrow-right-to-bracket fa-xl" /></router-link>
      </div>
      <div v-else>
        <button @click="logout"><i class="fa-solid fa-arrow-right-from-bracket fa-xl" /></button>
      </div>
    </nav>
  </section>
</template>
