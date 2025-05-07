<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'

// 장바구니 데이터 저장용 ref
const cartItems = ref([])

// 백엔드에서 장바구니 데이터 가져오기
const fetchCartItems = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/shopping', {
      withCredentials: true, // 세션 쿠키 포함
    })
    cartItems.value = response.data.map((item) => ({
      ...item,
      items: JSON.parse(item.items), // JSON 문자열을 객체 배열로 변환
    }))
  } catch (error) {
    console.error('장바구니 데이터 가져오기 실패:', error)
    if (error.response?.status === 401) {
      alert('로그인이 필요합니다.')
      window.location.href = '/login'
    } else {
      alert('로그인이 필요합니다')
      window.location.href = '/login'
    }
  }
}

// 장바구니 항목 삭제
const removeFromCart = async (id) => {
  if (!confirm('이 항목을 장바구니에서 삭제하시겠습니까?')) return
  try {
    await axios.delete(`http://localhost:8080/api/shopping/${id}`, {
      withCredentials: true,
    })
    cartItems.value = cartItems.value.filter((item) => item.id !== id)
    alert('항목이 삭제되었습니다.')
  } catch (error) {
    console.error('삭제 실패:', error)
    alert('항목 삭제에 실패했습니다.')
  }
}

// 컴포넌트 마운트 시 데이터 가져오기
onMounted(() => {
  fetchCartItems()
})
</script>

<template>
  <Header />
  <div class="flex flex-col items-center justify-center px-30">
    <section class="w-full h-full flex mt-10">
      <div class="w-4/5 mx-auto">
        <h2 class="text-2xl font-bold mb-4">내 장바구니</h2>
        <ul v-if="cartItems.length > 0" class="space-y-4">
          <li v-for="cart in cartItems" :key="cart.id" class="p-4 bg-gray-100 rounded-lg">
            <div class="flex justify-between items-center">
              <span class="font-semibold">주문 #{{ cart.id }}</span>
              <button @click="removeFromCart(cart.id)" class="text-red-500 hover:text-red-700 font-bold">X</button>
            </div>
            <ul class="mt-2 space-y-2">
              <li v-for="(item, idx) in cart.items" :key="idx" class="flex justify-between">
                <span>{{ item.name }}</span>
                <span>{{ item.price }} 원</span>
              </li>
            </ul>
            <div class="mt-2 text-right font-bold">총액: {{ cart.total.toLocaleString() }} 원</div>
          </li>
        </ul>
        <p v-else class="text-gray-500">장바구니가 비어 있습니다.</p>
      </div>
    </section>
  </div>
  <Footer />
</template>

<style scoped>
@import 'tailwindcss';
</style>
