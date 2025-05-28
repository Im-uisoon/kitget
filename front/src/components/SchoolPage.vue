<script setup>
import { useRoute, useRouter } from 'vue-router'
import Header from './view/Header.vue'
import { ref, onMounted, watch, computed } from 'vue'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

// 학교 식별
const schoolCode = route.params.schoolCode
const schools = {
  '001': '달천초등학교',
  '002': '탄금중학교',
  '003': '국원고등학교',
  '004': '한국교통대학교',
}
const schoolName = schools[schoolCode] || '알 수 없는 학교'
const pickupPlaces = {
  '001': '달천문구점',
  '002': '탄금문방구',
  '003': '다이소 충주점',
  '004': '국원마트',
}

// 학교 과목 조회
const classes = ref([])
const fetchClasses = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/api/shopping/${schoolCode}`)
    classes.value = response.data
  } catch (error) {
    console.error(error)
  }
}

// 학교 과목 선택
const selectedClass = ref('')
watch(selectedClass, (newClass) => {
  if (newClass) {
    fetchItems(newClass)
  } else {
    items.value = []
  }
})

// 준비물 조회
const items = ref([])
const fetchItems = async (className) => {
  try {
    const response = await axios.get(`http://localhost:8080/api/shopping/${schoolCode}/${className}/items`)
    const uniqueItems = []
    const seen = new Set()
    for (const item of response.data) {
      if (!seen.has(item.name)) {
        uniqueItems.push(item)
        seen.add(item.name)
      }
    }
    items.value = uniqueItems
  } catch (error) {
    console.error('Error fetching items:', error)
    items.value = []
  }
}

// 준비물 선택
const selectedItems = ref([])
const toggleItem = (item) => {
  const idx = selectedItems.value.findIndex((i) => i.name === item.name)
  if (idx === -1) {
    selectedItems.value.push(item)
  } else {
    selectedItems.value.splice(idx, 1)
  }
}

// 총 금액
const totalPrice = computed(() => selectedItems.value.reduce((sum, item) => sum + item.price, 0))

// 결제수단 선택
const paymentMethod = ref('')
const selectPayment = (method) => {
  paymentMethod.value = method
  if (method !== 'card') cardNumber.value = ''
}

// 배송방법 선택
const shippingMethod = ref('')
const postplace = ref('')
const selectShipping = (method) => {
  shippingMethod.value = method
  if (method === 'pickup') {
    postplace.value = pickupPlaces[schoolCode] || ''
  } else if (method === 'delivery') {
    postplace.value = ''
  }
}

// 주문하기 버튼 활성화
const cardNumber = ref('')
const isOrderEnabled = computed(() => selectedItems.value.length > 0 && !!paymentMethod.value && !!shippingMethod.value && (paymentMethod.value !== 'card' || cardNumber.value.trim().length > 0) && (shippingMethod.value !== 'delivery' || postplace.value.trim().length > 0))

// 백엔드 전송 자료 (/api/order)
// 기본 정보 (학교코드schoolCode, {상품이름name,가격price}, 결제방식paymentMethod, 배송방식shippingMethod)
// 추가 정보 (카드선택시-카드번호cardNumber, 택배선택시-배송주소postplace)
const submitOrder = async () => {
  const orderData = {
    schoolCode,
    items: selectedItems.value,
    paymentMethod: paymentMethod.value,
    shippingMethod: shippingMethod.value,
  }
  if (paymentMethod.value === 'card') {
    orderData.cardNumber = cardNumber.value
  }
  if (shippingMethod.value === 'delivery') {
    orderData.postplace = postplace.value
  }
  try {
    await axios.post('http://localhost:8080/api/order', orderData, {
      withCredentials: true,
    })
    alert('주문이 완료되었습니다!')
    router.push('/about')
  } catch (error) {
    alert('주문에 실패했습니다.')
    console.error(error)
  }
}

onMounted(fetchClasses)
</script>

<template>
  <div class="flex flex-col h-full">
    <Header />
    <section class="px-10 pt-5">
      <!-- 1. 제목 -->
      <h1 class="text-center text-lg">{{ schoolName }} 준비물</h1>

      <!-- 2. 선택지 -->
      <div class="mt-4">
        <select v-model="selectedClass" class="border rounded p-2 w-full">
          <option value="" disabled>과목을 선택하세요</option>
          <option v-for="className in classes" :key="className" :value="className">
            {{ className }}
          </option>
        </select>
      </div>

      <!-- 3. 준비물 목록 -->
      <div v-if="items.length > 0" class="my-5">
        <ul class="p-3 list-disc list-inside grid grid-cols-2 gap-3 h-40 overflow-y-auto content-start">
          <li v-for="item in items" :key="item.name" @click="toggleItem(item)" :class="['border-1 py-2 flex justify-center rounded-lg cursor-pointer select-none hover:shadow-lg px-3', selectedItems.some((i) => i.name === item.name) ? 'bg-blue-400 text-white border-1 border-black' : '']">
            <span>{{ item.name }}</span>
          </li>
        </ul>
      </div>

      <!-- 4. 주문 내역 -->
      <div v-if="selectedItems.length > 0" class="mt-4 border-t pt-2">
        <h3 class="text-md font-bold mb-2">주문 목록</h3>
        <ul class="overflow-y-auto h-20 content-start">
          <li v-for="item in selectedItems" :key="item.name" class="flex justify-between">
            <span>{{ item.name }}</span>
            <span>{{ item.price.toLocaleString() }}원</span>
          </li>
        </ul>
        <div class="flex justify-between mt-2 font-bold">
          <span>총 금액</span>
          <span>{{ totalPrice.toLocaleString() }}원</span>
        </div>
      </div>

      <!-- 5. 결제 방식 -->
      <div class="mt-4 border-t pt-5">
        <h1 class="font-bold">결제 방식 <span class="text-red-500">*</span></h1>
        <div class="flex justify-between pt-3 gap-3">
          <!-- 5.1. 카드/현장 선택 -->
          <div @click="selectPayment('card')" :class="['border-1 py-2 w-1/2 flex justify-center rounded-lg cursor-pointer select-none hover:shadow-lg', paymentMethod === 'card' ? 'bg-blue-400 text-white border-black' : '']" role="radio" :aria-checked="paymentMethod === 'card'" tabindex="0">카드</div>
          <div @click="selectPayment('money')" :class="['border-1 py-2 w-1/2 flex justify-center rounded-lg cursor-pointer select-none hover:shadow-lg', paymentMethod === 'money' ? 'bg-blue-400 text-white border-black' : '']" role="radio" :aria-checked="paymentMethod === 'money'" tabindex="0">현장결제</div>
        </div>

        <!-- 5.2. 선택 입력 -->
        <div class="mt-3">
          <template v-if="paymentMethod === 'card'">
            <label class="block mb-1 font-medium" for="card-number">카드 번호 <span class="text-red-500">*</span></label>
            <input id="card-number" v-model="cardNumber" type="text" maxlength="19" placeholder="카드 번호를 입력하세요" class="border rounded px-3 py-2 w-full" autocomplete="cc-number" />
          </template>
          <template v-else-if="paymentMethod === 'money'">
            <div class="text-blue-700 font-semibold">픽업/기사와 결제 해주세요</div>
          </template>
        </div>
      </div>

      <!-- 6.1. 배송 방식 -->
      <div class="mt-4 border-t pt-5">
        <h1 class="font-bold">배송 <span class="text-red-500">*</span></h1>
        <div class="flex justify-between pt-3 gap-3">
          <div @click="selectShipping('pickup')" :class="['border-1 py-2 w-1/2 flex justify-center rounded-lg cursor-pointer select-none hover:shadow-lg', shippingMethod === 'pickup' ? 'bg-blue-400 text-white border-black' : '']">픽업</div>
          <div @click="selectShipping('delivery')" :class="['border-1 py-2 w-1/2 flex justify-center rounded-lg cursor-pointer select-none hover:shadow-lg', shippingMethod === 'delivery' ? 'bg-blue-400 text-white border-black' : '']">택배</div>
        </div>

        <!-- 6.2. 배송 정보 입력 -->
        <div class="mt-3">
          <template v-if="shippingMethod === 'pickup'">
            <div class="text-blue-700 font-semibold">픽업 장소: {{ pickupPlaces[schoolCode] || '픽업 장소 미지정' }}</div>
            <input type="hidden" v-model="postplace" />
          </template>

          <template v-else-if="shippingMethod === 'delivery'">
            <label class="block mb-1 font-medium">배송지 주소 <span class="text-red-500">*</span></label>
            <input v-model="postplace" type="text" placeholder="주소를 입력하세요" class="border rounded px-3 py-2 w-full" />
          </template>
        </div>
      </div>

      <!-- 7. 주문완료 -->
      <div class="mt-8 flex justify-center">
        <button :disabled="!isOrderEnabled" @click="submitOrder" :class="['w-full py-3 rounded-lg font-bold transition-colors duration-200 mb-5', isOrderEnabled ? 'bg-blue-500 text-white cursor-pointer hover:bg-blue-400 border-1 border-black' : 'bg-gray-300 text-gray-500 cursor-not-allowed']">주문하기</button>
      </div>
    </section>
  </div>
</template>
