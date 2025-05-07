<script setup>
import { ref, computed } from 'vue'
import axios from 'axios'
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'

// 선택된 버튼의 인덱스를 저장하는 반응형 변수
const selectedButton = ref(0)
// 선택된 물건들을 저장하는 배열
const selectedItems = ref([])
// 장바구니에 담긴 물건들
const cart = ref([])

// 버튼 클릭 시 호출되는 함수
const selectButton = (index) => {
  selectedButton.value = index
  selectedItems.value = [] // 카테고리 변경 시 선택 초기화
}

// 물건 클릭 시 호출되는 함수 (선택/취소 토글)
const toggleItemSelection = (item, category) => {
  const itemKey = `${category}-${item}` // 고유 키 생성
  const index = selectedItems.value.indexOf(itemKey)
  if (index === -1) {
    selectedItems.value.push(itemKey) // 선택 추가
  } else {
    selectedItems.value.splice(index, 1) // 선택 취소
  }
}

// 장바구니에 담기 함수
const addToCart = () => {
  const itemsToAdd = selectedItems.value.map((key) => {
    const [category, itemName] = key.split('-')
    const menus = [Menu, Menu2, Menu3, Menu4, Menu5, Menu6, Menu7, Menu8]
    const reads = [Read, Read2, Read3, Read4, Read5, Read6, Read7, Read8]
    const moneys = [Money, Money2, Money3, Money4, Money5, Money6, Money7, Money8]
    const menu = menus[selectedButton.value]
    const read = reads[selectedButton.value]
    const money = moneys[selectedButton.value]
    const index = menu.indexOf(itemName)
    return {
      name: itemName,
      description: read[index],
      price: money[index],
    }
  })
  cart.value.push(...itemsToAdd)
  selectedItems.value = [] // 담은 후 선택 초기화
  alert(`${itemsToAdd.length}개의 물건이 장바구니에 담겼습니다!`)
}

// 장바구니에서 물건 제거 함수
const removeFromCart = (index) => {
  cart.value.splice(index, 1) // 해당 인덱스의 물건 제거
}

// 백엔드로 장바구니 데이터 저장 함수
const saveCart = async () => {
  if (cart.value.length === 0) {
    alert('장바구니가 비어 있습니다. 저장할 항목이 없습니다.')
    return
  }

  // 로그인 상태 확인
  try {
    const sessionResponse = await axios.get('http://localhost:8080/api/session-check', {
      withCredentials: true, // 세션 쿠키 포함
    })

    if (!sessionResponse.data.isAuthenticated) {
      alert('로그인이 필요합니다. 로그인 페이지로 이동합니다.')
      window.location.href = '/login' // 로그인 페이지로 리다이렉트
      return
    }

    // 로그인 상태가 확인되면 장바구니 저장 요청
    const response = await fetch('http://localhost:8080/api/shopping', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        // 세션 쿠키는 withCredentials로 자동 포함됨
      },
      credentials: 'include', // 세션 쿠키를 요청에 포함
      body: JSON.stringify({
        items: cart.value, // 장바구니 데이터
        total: parseInt(totalPrice.value.replace(',', '')), // 총 금액 (쉼표 제거)
      }),
    })

    if (!response.ok) {
      throw new Error('저장에 실패했습니다.')
    }

    const result = await response.json()
    alert('장바구니가 성공적으로 저장되었습니다!')
  } catch (error) {
    console.error('저장 중 오류 발생:', error)
    alert('로그인 상태를 확인해주세요')
  }
}

// 총 금액 계산 (computed로 반응형으로 관리)
const totalPrice = computed(() => {
  return cart.value
    .reduce((sum, item) => {
      const price = parseInt(item.price.replace(',', '')) // 쉼표 제거 후 숫자로 변환
      return sum + price
    }, 0)
    .toLocaleString() // 천 단위 쉼표 추가
})

// 개별 상품 데이터
const Menu = ['ATMEGA128', 'AVR MEGA', 'Rasberry PICO', 'Arduino UNO']
const Menu2 = ['LED 파랑', 'LED 초록', 'LCD', 'LED 빨강', 'LED 노랑', '칩LED']
const Menu3 = ['조도센서', '압력센서', '온도센서', '전류센서']
const Menu4 = ['구리선', '무연납', '납땜 키트', '스탠드']
const Read = ['ATmega128 MCU 모듈형 인터페이스', 'MCU를 사용하는 개발키드', '고성능 저전력 RP2350 칩셋', '아두이노우노 호환보드']
const Read2 = ['파란색 LED', '초록색 LED', 'LCD 패널', '빨간 LED', '노란 LED', '칩 모양 LED']
const Read3 = ['빛 감지 센서', '압력 감지 센서', '온도 감지 센서', '전류 감지 센서']
const Read4 = ['극세선 난연성 전선', '실습용 무연납', '납땜세트D형', '기판 고정 스탠드']
const Money = ['29,790', '66,300', '9,800', '9,900']
const Money2 = ['70', '40', '2,500', '60', '60', '40']
const Money3 = ['60', '7,200', '5,100', '4,680']
const Money4 = ['11,900', '10,000', '45,000', '13,500']

// 수업 상품 데이터
const Menu5 = ['ATMEGA128', 'AVR MEGA', 'Rasberry PICO', 'Arduino UNO', '구리선', '무연납', '납땜 키트', '스탠드']
const Menu6 = ['ATMEGA128', 'AVR MEGA', '구리선', '무연납', '납땜 키트']
const Menu7 = ['조도센서', '압력센서', '온도센서', '전류센서', 'LED 파랑']
const Menu8 = ['ATMEGA128']
const Read5 = ['ATmega128 MCU 모듈형 인터페이스', 'MCU를 사용하는 개발키드', '고성능 저전력 RP2350 칩셋', '아두이노우노 호환보드', '극세선 난연성 전선', '실습용 무연납', '납땜세트D형', '기판 고정 스탠드']
const Read6 = ['ATmega128 MCU 모듈형 인터페이스', 'MCU를 사용하는 개발키드', '극세선 난연성 전선', '실습용 무연납', '납땜세트D형']
const Read7 = ['빛 감지 센서', '압력 감지 센서', '온도 감지 센서', '전류 감지 센서', '파란색 LED']
const Read8 = ['ATmega128 MCU 모듈형 인터페이스']
const Money5 = ['29,790', '66,300', '9,800', '9,900', '11,900', '10,000', '45,000', '13,500']
const Money6 = ['29,790', '66,300', '11,900', '10,000', '45,000']
const Money7 = ['60', '7,200', '5,100', '4,680', '70']
const Money8 = ['29,790']
</script>

<template>
  <Header />
  <div class="flex flex-col items-center justify-center px-30">
    <section class="w-full h-full flex mt-10">
      <!-- 왼쪽 버튼 영역 -->
      <aside class="w-1/5 pr-5">
        <ul class="flex flex-col gap-3">
          <li>
            <button :class="['p-3 w-full text-left rounded-lg transition-colors', selectedButton === 0 ? 'bg-blue-600 text-white' : 'bg-gray-200 hover:bg-gray-300']" @click="selectButton(0)">개발보드</button>
          </li>
          <li>
            <button :class="['p-3 w-full text-left rounded-lg transition-colors', selectedButton === 1 ? 'bg-blue-600 text-white' : 'bg-gray-200 hover:bg-gray-300']" @click="selectButton(1)">LED/LCD</button>
          </li>
          <li>
            <button :class="['p-3 w-full text-left rounded-lg transition-colors', selectedButton === 2 ? 'bg-blue-600 text-white' : 'bg-gray-200 hover:bg-gray-300']" @click="selectButton(2)">센서</button>
          </li>
          <li>
            <button :class="['p-3 w-full text-left rounded-lg transition-colors', selectedButton === 3 ? 'bg-blue-600 text-white' : 'bg-gray-200 hover:bg-gray-300']" @click="selectButton(3)">납땜도구</button>
          </li>
        </ul>
        <hr class="mt-10 p-3" />
        <ul class="flex flex-col gap-3 mt-3">
          <li>
            <button :class="['p-3 w-full text-left rounded-lg transition-colors', selectedButton === 4 ? 'bg-blue-600 text-white' : 'bg-gray-200 hover:bg-gray-300']" @click="selectButton(4)">디지털회로설계</button>
          </li>
          <li>
            <button :class="['p-3 w-full text-left rounded-lg transition-colors', selectedButton === 5 ? 'bg-blue-600 text-white' : 'bg-gray-200 hover:bg-gray-300']" @click="selectButton(5)">임베디드프로그래밍</button>
          </li>
          <li>
            <button :class="['p-3 w-full text-left rounded-lg transition-colors', selectedButton === 6 ? 'bg-blue-600 text-white' : 'bg-gray-200 hover:bg-gray-300']" @click="selectButton(6)">디지털신호</button>
          </li>
          <li>
            <button :class="['p-3 w-full text-left rounded-lg transition-colors', selectedButton === 7 ? 'bg-blue-600 text-white' : 'bg-gray-200 hover:bg-gray-300']" @click="selectButton(7)">마이크로프로세서</button>
          </li>
        </ul>
      </aside>

      <!-- 오른쪽 물품 영역 -->
      <div class="w-4/5 h-170">
        <!-- 1 개발보드 -->
        <div v-if="selectedButton === 0" class="grid grid-cols-4 gap-6">
          <div v-for="(item, index) in Menu" :key="index" class="selectColBox" :class="{ 'bg-blue-200 border-2 border-blue-400': selectedItems.includes(`0-${item}`) }" @click="toggleItemSelection(item, 0)">
            <img :src="`/src/assets/image/${['avr', 'atmega', 'pico', 'uno'][index]}.jpg`" class="w-40 h-40 mx-auto" />
            <p class="font-bold text-xl pt-3 text-center">{{ item }}</p>
            <p class="text-sm pt-2 text-gray-600 text-center">{{ Read[index] }}</p>
            <p class="pt-2 text-center">{{ Money[index] }} 원</p>
          </div>
        </div>

        <!-- 2 LED/LCD -->
        <div v-if="selectedButton === 1" class="grid grid-cols-4 gap-6">
          <div v-for="(item, index) in Menu2" :key="index" class="selectColBox" :class="{ 'bg-blue-200 border-2 border-blue-400': selectedItems.includes(`1-${item}`) }" @click="toggleItemSelection(item, 1)">
            <img :src="`/src/assets/image/${['blue', 'green', 'lcd', 'red', 'yellow', 'chipled'][index]}.jpg`" class="w-40 h-40 mx-auto" />
            <p class="font-bold text-xl pt-3 text-center">{{ item }}</p>
            <p class="text-sm pt-2 text-gray-600 text-center">{{ Read2[index] }}</p>
            <p class="pt-2 text-center">{{ Money2[index] }} 원</p>
          </div>
        </div>

        <!-- 3 센서 -->
        <div v-if="selectedButton === 2" class="grid grid-cols-4 gap-6">
          <div v-for="(item, index) in Menu3" :key="index" class="selectColBox" :class="{ 'bg-blue-200 border-2 border-blue-400': selectedItems.includes(`2-${item}`) }" @click="toggleItemSelection(item, 2)">
            <img :src="`/src/assets/image/${['jodo', 'press', 'temper', 'vol'][index]}.jpg`" class="w-40 h-40 mx-auto" />
            <p class="font-bold text-xl pt-3 text-center">{{ item }}</p>
            <p class="text-sm pt-2 text-gray-600 text-center">{{ Read3[index] }}</p>
            <p class="pt-2 text-center">{{ Money3[index] }} 원</p>
          </div>
        </div>

        <!-- 4 납땜도구 -->
        <div v-if="selectedButton === 3" class="grid grid-cols-4 gap-6">
          <div v-for="(item, index) in Menu4" :key="index" class="selectColBox" :class="{ 'bg-blue-200 border-2 border-blue-400': selectedItems.includes(`3-${item}`) }" @click="toggleItemSelection(item, 3)">
            <img :src="`/src/assets/image/${['goori', 'nap', 'napkit', 'stand'][index]}.jpg`" class="w-40 h-40 mx-auto" />
            <p class="font-bold text-xl pt-3 text-center">{{ item }}</p>
            <p class="text-sm pt-2 text-gray-600 text-center">{{ Read4[index] }}</p>
            <p class="pt-2 text-center">{{ Money4[index] }} 원</p>
          </div>
        </div>

        <!-- 5 디지털회로설계 -->
        <div v-if="selectedButton === 4" class="grid grid-cols-4 gap-6">
          <div v-for="(item, index) in Menu5" :key="index" class="selectColBox" :class="{ 'bg-blue-200 border-2 border-blue-400': selectedItems.includes(`4-${item}`) }" @click="toggleItemSelection(item, 4)">
            <img :src="`/src/assets/image/${['avr', 'atmega', 'pico', 'uno', 'goori', 'nap', 'napkit', 'stand'][index]}.jpg`" class="w-40 h-40 mx-auto" />
            <p class="font-bold text-xl pt-3 text-center">{{ item }}</p>
            <p class="text-sm pt-2 text-gray-600 text-center">{{ Read5[index] }}</p>
            <p class="pt-2 text-center">{{ Money5[index] }} 원</p>
          </div>
        </div>

        <!-- 6 임베디드프로그래밍 -->
        <div v-if="selectedButton === 5" class="grid grid-cols-4 gap-6">
          <div v-for="(item, index) in Menu6" :key="index" class="selectColBox" :class="{ 'bg-blue-200 border-2 border-blue-400': selectedItems.includes(`5-${item}`) }" @click="toggleItemSelection(item, 5)">
            <img :src="`/src/assets/image/${['avr', 'atmega', 'goori', 'nap', 'napkit'][index]}.jpg`" class="w-40 h-40 mx-auto" />
            <p class="font-bold text-xl pt-3 text-center">{{ item }}</p>
            <p class="text-sm pt-2 text-gray-600 text-center">{{ Read6[index] }}</p>
            <p class="pt-2 text-center">{{ Money6[index] }} 원</p>
          </div>
        </div>

        <!-- 7 디지털신호 -->
        <div v-if="selectedButton === 6" class="grid grid-cols-4 gap-6">
          <div v-for="(item, index) in Menu7" :key="index" class="selectColBox" :class="{ 'bg-blue-200 border-2 border-blue-400': selectedItems.includes(`6-${item}`) }" @click="toggleItemSelection(item, 6)">
            <img :src="`/src/assets/image/${['jodo', 'press', 'temper', 'vol', 'blue'][index]}.jpg`" class="w-40 h-40 mx-auto" />
            <p class="font-bold text-xl pt-3 text-center">{{ item }}</p>
            <p class="text-sm pt-2 text-gray-600 text-center">{{ Read7[index] }}</p>
            <p class="pt-2 text-center">{{ Money7[index] }} 원</p>
          </div>
        </div>

        <!-- 8 마이크로프로세서 -->
        <div v-if="selectedButton === 7" class="grid grid-cols-4 gap-6">
          <div v-for="(item, index) in Menu8" :key="index" class="selectColBox" :class="{ 'bg-blue-200 border-2 border-blue-400': selectedItems.includes(`7-${item}`) }" @click="toggleItemSelection(item, 7)">
            <img :src="`/src/assets/image/${['atmega'][index]}.jpg`" class="w-40 h-40 mx-auto" />
            <p class="font-bold text-xl pt-3 text-center">{{ item }}</p>
            <p class="text-sm pt-2 text-gray-600 text-center">{{ Read8[index] }}</p>
            <p class="pt-2 text-center">{{ Money8[index] }} 원</p>
          </div>
        </div>
      </div>
    </section>
    <!-- 담기 및 저장 버튼 -->
    <div class="mt-6 text-center space-x-4">
      <button class="p-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700" @click="addToCart" :disabled="selectedItems.length === 0">장바구니에 담기</button>
      <button class="p-3 bg-green-600 text-white rounded-lg hover:bg-green-700" @click="saveCart" :disabled="cart.length === 0">저장하기</button>
    </div>

    <!-- 장바구니 표시 -->
    <div v-if="cart.length > 0" class="mt-6 w-4/5">
      <div class="flex justify-between items-center">
        <h3 class="text-lg font-bold">장바구니</h3>
        <p class="text-lg">총 금액: {{ totalPrice }} 원</p>
      </div>
      <ul class="mt-2">
        <li v-for="(item, index) in cart" :key="index" class="p-2 bg-gray-100 rounded-lg mb-2 flex justify-between items-center">
          <span>{{ item.name }} - {{ item.price }} 원</span>
          <button @click="removeFromCart(index)" class="text-red-500 hover:text-red-700 font-bold">X</button>
        </li>
      </ul>
    </div>
  </div>
  <Footer />
</template>

<style scoped>
@import 'tailwindcss';

.selectColBox {
  @apply flex flex-col items-center justify-center p-4 bg-white rounded-lg shadow-md cursor-pointer transition-colors;
}

.selectColBox:hover {
  @apply bg-gray-50;
}
</style>
