<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import Header from './view/Header.vue'

const router = useRouter()
const school = ref(null)

const goToShop = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/user/school', { withCredentials: true })
    const schoolCode = response.data.school
    if (schoolCode) {
      router.push(`/school${schoolCode}`)
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
    <section class="h-3/5 p-10 pt-20 select-none">
      <h1 class="font-[LogoFont] text-5xl mb-5">KitGet</h1>
      <h2 class="font-[LogoFont] text-2xl mb-2">필요한 <strong class="text-blue-500">Kit</strong></h2>
      <h3 class="font-[LogoFont] text-2xl mb-10">간편하게 <strong class="text-blue-500">Get</strong></h3>

      <router-link to="#" @click.prevent="goToShop" class="font-[LogoFont] text-xl underline underline-offset-8">구매하기 →</router-link>
    </section>

    <!-- 3. 메뉴 -->
    <section class="bg-blue-400 rounded-t-lg flex-1 flex justify-center items-center select-none">
      <div class="grid grid-cols-2 gap-5">
        <router-link to="#" class="bg-white rounded-lg text-center text-black py-3 px-8 hover:shadow-lg">할일</router-link>
        <router-link to="#" class="bg-white rounded-lg text-center text-black py-3 px-8 hover:shadow-lg">공지</router-link>
        <router-link to="/order" class="bg-white rounded-lg text-center text-black py-3 px-8 hover:shadow-lg">주문내역</router-link>
        <router-link to="#" class="bg-white rounded-lg text-center text-black py-3 px-8 hover:shadow-lg">설정</router-link>
      </div>
    </section>
  </nav>
</template>
