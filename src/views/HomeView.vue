<script setup>
import { ref } from 'vue'
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'

// 선택된 버튼의 인덱스를 저장하는 반응형 변수
const selectedButton = ref(0)

// 버튼 클릭 시 호출되는 함수
const selectButton = (index) => {
  selectedButton.value = index
}

// 개별 상품
const Menu = ['ATMEGA128', 'AVR MEGA', 'Rasberry PICO', 'Arduino UNO']
const Menu2 = ['LED 파랑', 'LED 초록', 'LCD', 'LED 빨강']
const Menu3 = ['조도센서', '압력센서', '온도센서', '전류센서']
const Menu4 = ['구리선', '무연납', '납땜 키트', '스탠드']
const Read = ['ATmega128 MCU 모듈형 인터페이스', 'MCU를 사용하는 개발키드', '고성능 저전력 RP2350 칩셋', '아두이노우노 호환보드']
const Read2 = ['파란색 LED', '초록색 LED', 'LCD 패널', '빨간 LED']
const Read3 = ['빛 감지 센서', '압력 감지 센서', '온도 감지 센서', '전류 감지 센서']
const Read4 = ['극세선 난연성 전선', '실습용 무연납', '납땜세트D형', '기판 고정 스탠드']
const Money = ['29,790', '66,300', '9,800', '9,900']
const Money2 = ['70', '40', '2,500', '60']
const Money3 = ['60', '7,200', '5,100', '4,680']
const Money4 = ['11,900', '10,000', '45,000', '13,500']
</script>

<template>
  <!-- header -->
  <Header />

  <!-- 인트로 배너 -->
  <section class="introBox bg-browny">
    <!-- 좌측 메세지 -->
    <div class="h-full w-1/2">
      <h1 class="text-white text-3xl select-none">KitGet은 편의성을 최우선으로 합니다.</h1>
      <p class="pt-5 mt-5 text-white select-none">
        학생들이 부품을 잘못 구매하거나 수업 시간에 준비물을 <br />
        누락하는 상황이 발생하지 않도록 세심하게 지원합니다. <br />
        우리는 'Simple & Best'라는 목표를 향해 나아갑니다.
      </p>
      <h2 class="flex flex-col gap-3 pt-6 mt-6 text-white">
        <router-link to="/store" class="underline underline-offset-4">구매하기</router-link>
        <router-link to="/resell" class="underline underline-offset-4">되팔기</router-link>
      </h2>
    </div>

    <!-- 우측 사진 -->
    <div class="flex justify-end h-full w-1/2 pr-10">
      <img src="/src/assets/image/intro.png" />
    </div>
  </section>

  <!-- 구매 화면 -->
  <section class="buyBox">
    <ul class="flex gap-30 justify-center p-3 pb-0">
      <li>
        <button :class="['p-3 w-24', selectedButton === 0]" @click="selectButton(0)">개발보드</button>
      </li>
      <li>
        <button :class="['p-3 w-24', selectedButton === 1]" @click="selectButton(1)">LED/LCD</button>
      </li>
      <li>
        <button :class="['p-3 w-24', selectedButton === 2]" @click="selectButton(2)">센서</button>
      </li>
      <li>
        <button :class="['p-3 w-24', selectedButton === 3]" @click="selectButton(3)">납땜도구</button>
      </li>
    </ul>

    <!-- 1 개발보드 -->
    <div v-if="selectedButton === 0" class="selectBox">
      <div class="selectColBox">
        <img src="/src/assets/image/avr.jpg" class="w-40 h-40" />
        <p class="font-bold text-2xl pt-3">{{ Menu[0] }}</p>
        <p class="text-sm pt-2">{{ Read[0] }}</p>
        <p class="pt-2">{{ Money[0] }} 원</p>
      </div>
      <div class="selectColBox">
        <img src="/src/assets/image/atmega.jpg" class="w-40 h-40" />
        <p class="font-bold text-2xl pt-3">{{ Menu[1] }}</p>
        <p class="text-sm pt-2">{{ Read[1] }}</p>
        <p class="pt-2">{{ Money[1] }} 원</p>
      </div>
      <div class="selectColBox">
        <img src="/src/assets/image/pico.jpg" class="w-40 h-40" />
        <p class="font-bold text-2xl pt-3">{{ Menu[2] }}</p>
        <p class="text-sm pt-2">{{ Read[2] }}</p>
        <p class="pt-2">{{ Money[2] }} 원</p>
      </div>
      <div class="selectColBox">
        <img src="/src/assets/image/uno.jpg" class="w-40 h-40" />
        <p class="font-bold text-2xl pt-3">{{ Menu[3] }}</p>
        <p class="text-sm pt-2">{{ Read[3] }}</p>
        <p class="pt-2">{{ Money[3] }} 원</p>
      </div>
    </div>

    <!-- 2 LED -->
    <div v-if="selectedButton === 1" class="selectBox">
      <div class="selectColBox">
        <img src="/src/assets/image/blue.jpg" class="w-40 h-40" />
        <p class="font-bold text-2xl pt-3">{{ Menu2[0] }}</p>
        <p class="text-sm pt-2">{{ Read2[0] }}</p>
        <p class="pt-2">{{ Money2[0] }} 원</p>
      </div>
      <div class="selectColBox">
        <img src="/src/assets/image/green.jpg" class="w-40 h-40" />
        <p class="font-bold text-2xl pt-3">{{ Menu2[1] }}</p>
        <p class="text-sm pt-2">{{ Read2[1] }}</p>
        <p class="pt-2">{{ Money2[1] }} 원</p>
      </div>
      <div class="selectColBox">
        <img src="/src/assets/image/lcd.jpg" class="w-40 h-40" />
        <p class="font-bold text-2xl pt-3">{{ Menu2[2] }}</p>
        <p class="text-sm pt-2">{{ Read2[2] }}</p>
        <p class="pt-2">{{ Money2[2] }} 원</p>
      </div>
      <div class="selectColBox">
        <img src="/src/assets/image/red.jpg" class="w-40 h-40" />
        <p class="font-bold text-2xl pt-3">{{ Menu2[3] }}</p>
        <p class="text-sm pt-2">{{ Read2[3] }}</p>
        <p class="pt-2">{{ Money2[3] }} 원</p>
      </div>
    </div>

    <!-- 3 센서 -->
    <div v-if="selectedButton === 2" class="selectBox">
      <div class="selectColBox">
        <img src="/src/assets/image/jodo.jpg" class="w-40 h-40" />
        <p class="font-bold text-2xl pt-3">{{ Menu3[0] }}</p>
        <p class="text-sm pt-2">{{ Read3[0] }}</p>
        <p class="pt-2">{{ Money3[0] }} 원</p>
      </div>
      <div class="selectColBox">
        <img src="/src/assets/image/press.jpg" class="w-40 h-40" />
        <p class="font-bold text-2xl pt-3">{{ Menu3[1] }}</p>
        <p class="text-sm pt-2">{{ Read3[1] }}</p>
        <p class="pt-2">{{ Money3[1] }} 원</p>
      </div>
      <div class="selectColBox">
        <img src="/src/assets/image/temper.jpg" class="w-40 h-40" />
        <p class="font-bold text-2xl pt-3">{{ Menu3[2] }}</p>
        <p class="text-sm pt-2">{{ Read3[2] }}</p>
        <p class="pt-2">{{ Money3[2] }} 원</p>
      </div>
      <div class="selectColBox">
        <img src="/src/assets/image/vol.jpg" class="w-40 h-40" />
        <p class="font-bold text-2xl pt-3">{{ Menu3[3] }}</p>
        <p class="text-sm pt-2">{{ Read3[3] }}</p>
        <p class="pt-2">{{ Money3[3] }} 원</p>
      </div>
    </div>

    <!-- 4 납땜 -->
    <div v-if="selectedButton === 3" class="selectBox">
      <div class="selectColBox">
        <img src="/src/assets/image/goori.jpg" class="w-40 h-40" />
        <p class="font-bold text-2xl pt-3">{{ Menu4[0] }}</p>
        <p class="text-sm pt-2">{{ Read4[0] }}</p>
        <p class="pt-2">{{ Money4[0] }} 원</p>
      </div>
      <div class="selectColBox">
        <img src="/src/assets/image/nap.jpg" class="w-40 h-40" />
        <p class="font-bold text-2xl pt-3">{{ Menu4[1] }}</p>
        <p class="text-sm pt-2">{{ Read4[1] }}</p>
        <p class="pt-2">{{ Money4[1] }} 원</p>
      </div>
      <div class="selectColBox">
        <img src="/src/assets/image/napkit.jpg" class="w-40 h-40" />
        <p class="font-bold text-2xl pt-3">{{ Menu4[2] }}</p>
        <p class="text-sm pt-2">{{ Read4[2] }}</p>
        <p class="pt-2">{{ Money4[2] }} 원</p>
      </div>
      <div class="selectColBox">
        <img src="/src/assets/image/stand.jpg" class="w-40 h-40" />
        <p class="font-bold text-2xl pt-3">{{ Menu4[3] }}</p>
        <p class="text-sm pt-2">{{ Read4[3] }}</p>
        <p class="pt-2">{{ Money4[3] }} 원</p>
      </div>
    </div>
  </section>

  <!-- 구매 안내 -->
  <section class="w-full h-80 bg-browny2 flex flex-col justify-center items-center">
    <h1 class="w-full justify-center text-center text-2xl">
      <p class="text-amber-950">간편히 구매하는, KitGet</p>
      <p class="pt-3 text-browny">다 사용했다면 되팔아보세요</p>
    </h1>
    <div class="pt-10 flex justify-center gap-50">
      <router-link to="/store" class="ring-2 ring-browny p-3 w-50 text-lg rounded-sm text-amber-950 text-center">제품 구매</router-link>
      <router-link to="/resell" class="ring-2 ring-browny p-3 w-50 text-lg rounded-sm text-white bg-browny text-center">중고 장터</router-link>
    </div>
  </section>

  <!-- footer -->
  <Footer />
</template>

<style scoped>
@import 'tailwindcss';

.introBox {
  @apply w-full h-100 flex flex-row justify-between py-15 px-50;
}

.buyBox {
  @apply h-80 mt-10 mb-30;
}

.selectBox {
  @apply h-full p-20 px-40 flex justify-between items-center gap-20;
}

.selectColBox {
  @apply w-1/4 flex items-center justify-center flex-col;
}
</style>
