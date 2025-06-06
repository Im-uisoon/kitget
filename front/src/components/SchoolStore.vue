<script setup>
import { ref } from 'vue'
import axios from 'axios'
import Header from './view/Header.vue'

const className = ref('')
const productName = ref('')
const price = ref('')
const successMsg = ref('')
const errorMsg = ref('')

const isEnabled = () => className.value.trim().length > 0 && productName.value.trim().length > 0 && price.value !== '' && !isNaN(Number(price.value)) && Number(price.value) > 0

const registerProduct = async () => {
  successMsg.value = ''
  errorMsg.value = ''
  try {
    await axios.post(
      'tender-hally-imuisoon-0eaa6cd0.koyeb.app/api/product/register',
      {
        className: className.value,
        productName: productName.value,
        price: Number(price.value),
      },
      { withCredentials: true },
    )
    successMsg.value = '등록 완료!'
    className.value = ''
    productName.value = ''
    price.value = ''
  } catch (e) {
    errorMsg.value = '상품 등록에 실패했습니다.'
  }
}
</script>

<template>
  <Header />
  <section class="pt-10 mx-10">
    <div class="max-w-lg mx-auto bg-white rounded-lg shadow p-8">
      <h2 class="text-2xl font-bold mb-6 text-center">상품 등록</h2>
      <div class="mb-4">
        <label class="block mb-1 font-semibold">수업명</label>
        <input v-model="className" type="text" class="border rounded px-3 py-2 w-full" placeholder="예) 수학" />
      </div>
      <div class="mb-4">
        <label class="block mb-1 font-semibold">상품명</label>
        <input v-model="productName" type="text" class="border rounded px-3 py-2 w-full" placeholder="예) 삼각자 세트" />
      </div>
      <div class="mb-4">
        <label class="block mb-1 font-semibold">가격</label>
        <input v-model="price" type="number" min="1" class="border rounded px-3 py-2 w-full" placeholder="예) 3000" />
      </div>
      <button @click="registerProduct" :disabled="!isEnabled()" :class="['w-full py-2 rounded font-bold transition-colors duration-200', isEnabled() ? 'bg-blue-500 text-white hover:bg-blue-600' : 'bg-gray-300 text-gray-500 cursor-not-allowed']">등록</button>
      <div v-if="successMsg" class="text-green-600 mt-4">{{ successMsg }}</div>
      <div v-if="errorMsg" class="text-red-600 mt-4">{{ errorMsg }}</div>
    </div>
  </section>
</template>
