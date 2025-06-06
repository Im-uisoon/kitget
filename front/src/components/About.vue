<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import Header from './view/Header.vue'

const router = useRouter()
const school = ref(null)
const userAuth = ref(null)

// 신원 조회
onMounted(async () => {
  const res = await axios.get('http://localhost:8080/api/me', { withCredentials: true })
  userAuth.value = res.data.auth
})

// 교사:교재등록(학교 조회 및 개별 링크)
const goToShop = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/user/school', { withCredentials: true })
    const schoolCode = response.data.school
    if (schoolCode) {
      router.push(`/school/${schoolCode}`)
    } else {
      router.push('/')
    }
  } catch (error) {
    console.error('학교 조회 에러:', error.response?.status, error.response?.data)
    router.push('/')
  }
}

// 교사:등록하기(학교 조회 및 개별 링크)
const goToStore = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/user/school', { withCredentials: true })
    const schoolCode = response.data.school
    if (schoolCode) {
      router.push(`/store/${schoolCode}`)
    } else {
      router.push('/')
    }
  } catch (error) {
    console.error('학교 조회 에러:', error.response?.status, error.response?.data)
    router.push('/')
  }
}
</script>

<template>
  <nav class="flex flex-col h-screen">
    <!-- 1. 헤더 -->
    <Header />

    <!-- 2. 메인 -->
    <section class="h-3/5 p-10 md:p-30 pt-20 select-none">
      <h1 class="font-[LogoFont] text-5xl md:text-7xl mb-5 md:mb-7">KitGet</h1>
      <h2 class="font-[LogoFont] text-2xl md:text-4xl mb-2 md:mb-5">필요한 <strong class="text-blue-500">Kit</strong></h2>
      <h3 class="font-[LogoFont] text-2xl md:text-4xl mb-10 md:mb-15">간편하게 <strong class="text-blue-500">Get</strong></h3>

      <div v-if="userAuth === 'teacher'">
        <router-link to="#" @click.prevent="goToStore" class="font-[LogoFont] text-xl md:text-3xl underline underline-offset-8">교재등록 →</router-link>
      </div>
      <div v-else>
        <router-link to="#" @click.prevent="goToShop" class="font-[LogoFont] text-xl md:text-3xl underline underline-offset-8">구매하기 →</router-link>
      </div>
    </section>

    <!-- 3. 메뉴 -->
    <section class="bg-blue-400 rounded-t-lg flex-1 flex justify-center items-center select-none">
      <div class="grid grid-cols-2 md:grid-cols-4 gap-5 md:gap-10">
        <router-link to="/todo" class="md:text-xl bg-white rounded-lg text-center text-black py-3 md:py-10 md:px-25 px-8 hover:shadow-lg">할일</router-link>
        <router-link to="/notice" class="md:text-xl bg-white rounded-lg text-center text-black py-3 md:py-10 md:px-25 px-8 hover:shadow-lg">공지</router-link>
        <router-link to="/order" class="md:text-xl bg-white rounded-lg text-center text-black py-3 md:py-10 md:px-25 px-8 hover:shadow-lg">주문내역</router-link>
        <router-link to="/settings" class="md:text-xl bg-white rounded-lg text-center text-black py-3 md:py-10 md:px-25 px-8 hover:shadow-lg">설정</router-link>
      </div>
    </section>
  </nav>
</template>
