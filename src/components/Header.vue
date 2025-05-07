<script setup>
import axios from 'axios'
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const openMenu = ref(false)
const isAuthenticated = ref(false)
const router = useRouter()
const searchText = ref('')

// 세션 확인 함수
const checkSession = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/session-check', {
      withCredentials: true,
    })
    isAuthenticated.value = response.data.isAuthenticated
  } catch (error) {
    console.error('Session check error:', error)
    isAuthenticated.value = false
  }
}

// 로그아웃 함수
const logout = async () => {
  try {
    await axios.get('http://localhost:8080/api/logout', { withCredentials: true })
    isAuthenticated.value = false // 즉시 상태 변경
    await checkSession() // 서버 상태 재확인 (선택적)
    router.push('/login')
  } catch (error) {
    console.error('Logout error:', error)
    isAuthenticated.value = false // 에러 시에도 상태 초기화
  }
}

// 검색 실행 함수
const performSearch = () => {
  if (searchText.value.trim() === '') {
    alert('검색어를 입력해주세요.')
    return
  }
  const url = `https://www.devicemart.co.kr/goods/search?keyword_log_flag=Y&search_text=${encodeURIComponent(searchText.value)}`
  window.open(url, '_blank')
}

// 컴포넌트 마운트 시 세션 확인
onMounted(() => {
  checkSession()
})
</script>

<template>
  <!-- 로고 / 검색 / 아이콘 -->
  <section class="flex justify-between items-center px-25 py-10">
    <!-- 로고 파트 -->
    <router-link to="/" class="flex">
      <p class="text-gray-800 text-5xl font-bold">Kit</p>
      <p class="text-browny text-5xl font-bold">Get</p>
      <i class="fa-solid fa-gear text-3xl text-gray-800 ml-2" />
    </router-link>
    <!-- 검색 파트 -->
    <div class="searchBox ring-browny">
      <input type="text" placeholder="디바이스마트에서 검색" class="ml-5 w-full p-2" v-model="searchText" @keyup.enter="performSearch" />
      <button @click="performSearch" class="p-2">
        <i class="fa-solid fa-magnifying-glass mx-5" />
      </button>
    </div>

    <!-- 아이콘 파트 -->
    <div class="flex text-2xl gap-6 text-gray-800">
      <!-- 장바구니 -->
      <div v-if="!isAuthenticated">
        <router-link to="/login"><i class="fa-solid fa-cart-shopping" /></router-link>
      </div>
      <div v-else>
        <router-link to="/shopping"><i class="fa-solid fa-cart-shopping" /></router-link>
      </div>
      <!-- 쪽지 -->
      <div v-if="!isAuthenticated">
        <router-link to="/login"><i class="fa-solid fa-envelope" /></router-link>
      </div>
      <div v-else>
        <router-link to="/letter"><i class="fa-solid fa-envelope" /></router-link>
      </div>
      <!-- 로그인/로그아웃 -->
      <div>
        <!-- 로그인 -->
        <div v-if="!isAuthenticated">
          <router-link to="/login"><i class="fa-solid fa-arrow-right-to-bracket" /></router-link>
        </div>
        <!-- 로그아웃 -->
        <div v-else>
          <button @click="logout"><i class="fa-solid fa-arrow-right-from-bracket" /></button>
        </div>
      </div>
    </div>
  </section>

  <!-- 메뉴바 -->
  <ul class="p-5 flex justify-center items-center gap-50 text-yellow-800">
    <!-- 소개글 -->
    <router-link to="/intro">Intro</router-link>
    <!-- 스토어 -->
    <router-link to="/store">Store</router-link>
    <!-- 중고 -->
    <router-link to="/resell">Resell</router-link>
    <!-- 고객(공지사항, 상담센터, 개인정보, 장바구니, 구매후기) -->
    <li class="relative">
      <button @click="openMenu = !openMenu" class="focus:outline-none">Customer <i class="fa-solid fa-caret-down" /></button>
      <!-- 드랍다운 메뉴 -->
      <div v-if="openMenu" class="absolute left-0 mt-2 w-48 rounded-md shadow-lg bg-white ring-1 ring-browny text-gray-800">
        <ul class="py-1">
          <li>
            <router-link to="/notice" class="block px-4 py-2 text-sm hover:bg-yellow-100">공지사항</router-link>
          </li>
          <li>
            <router-link to="/letter" class="block px-4 py-2 text-sm hover:bg-yellow-100">상담 메일</router-link>
          </li>
          <li>
            <router-link to="/shopping" class="block px-4 py-2 text-sm hover:bg-yellow-100">장바구니</router-link>
          </li>
          <li>
            <router-link to="/review" class="block px-4 py-2 text-sm hover:bg-yellow-100">구매후기</router-link>
          </li>
        </ul>
      </div>
    </li>
  </ul>
  <hr class="text-browny" />
</template>

<style scoped>
@import 'tailwindcss';

.adBox {
  @apply bg-emerald-500 h-12 flex justify-between items-center;
}

.searchBox {
  @apply flex justify-between items-center rounded-full ring-2 w-xl h-10;
}
</style>
