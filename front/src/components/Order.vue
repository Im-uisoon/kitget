<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import Header from './view/Header.vue'

const orders = ref([])

onMounted(async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/orders', {
      withCredentials: true,
    })
    orders.value = response.data
  } catch (error) {
    console.error('주문내역 불러오기 실패:', error)
  }
})

const Pay = (method) => {
  if (method === 'card') return '카드'
  if (method === 'money') return '현장결제'
  return method
}
const Shipping = (method) => {
  if (method === 'pickup') return '픽업'
  if (method === 'delivery') return '택배'
  return method
}

function formatOrderDate(dateStr) {
  const date = new Date(dateStr)
  const yy = String(date.getFullYear()).slice(2)
  const mm = String(date.getMonth() + 1).padStart(2, '0')
  const dd = String(date.getDate()).padStart(2, '0')
  const hh = String(date.getHours()).padStart(2, '0')
  const min = String(date.getMinutes()).padStart(2, '0')
  return `${yy}.${mm}.${dd} ${hh}:${min}`
}

async function cancelOrder(orderId) {
  if (!confirm('정말 주문을 취소하시겠습니까?')) return
  try {
    await axios.delete(`http://localhost:8080/api/orders/${orderId}`, {
      withCredentials: true,
    })
    alert('주문이 취소되었습니다.')
    window.location.reload()
  } catch (error) {
    alert('주문 취소에 실패했습니다.')
    console.error(error)
  }
}
</script>

<template>
  <!-- 헤더 -->
  <Header />

  <!-- 바디 -->
  <section class="px-10 pt-5">
    <h1 class="text-xl font-bold pb-5">주문 내역</h1>
    <!-- 주문 내역 없는 경우 -->
    <nav v-if="orders.length === 0" class="text-gray-500 text-center py-8">
      <p>주문 내역이 없습니다</p>
    </nav>
    <!-- 주문 내역 있을 경우 -->
    <nav v-else>
      <!-- 주문 상품 -->
      <div v-for="order in orders" :key="order.orderId" class="ring-1 ring-blue-400 rounded-md p-2 mb-3">
        <!-- 1행 -->
        <div class="flex justify-between">
          <div class="flex gap-1">
            <!-- 1. 주문번호 -->
            <p>주문번호 #{{ order.orderId }}</p>
            <!-- 2. 결제/배송 -->
            <p>{{ Pay(order.payment) }}</p>
            ·
            <p>{{ Shipping(order.shipping) }}</p>
          </div>
          <!-- 3. 주문취소 -->
          <button @click="cancelOrder(order.orderId)" class="select-none text-red-500 font-bold">X</button>
        </div>
        <!-- 2행 -->
        <ul class="pt-3">
          <li>{{ order.itemNames.join(', ') }}</li>
        </ul>
        <!-- 3행 -->
        <div class="flex justify-between pt-3">
          <p>{{ formatOrderDate(order.orderDate) }}</p>
          <p>
            총 금액 : <span>{{ order.totalPrice.toLocaleString() }}원</span>
          </p>
        </div>
      </div>
    </nav>
  </section>
</template>
