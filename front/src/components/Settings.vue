<script setup>
import { ref, computed } from 'vue'
import axios from 'axios'
import Header from './view/Header.vue'
import { useRouter } from 'vue-router'
const router = useRouter()
const API_BASE = 'http://localhost:8080/api'

/********************* 1. 기존 비밀번호 확인 *********************/
const isVerifying = ref(false)
const verifyError = ref('')
const currentPassword = ref('')
const isVerified = ref(false)
// 비밀번호 입력 후 인증(기본-변경 가능 비활성화)
const verifyPassword = async () => {
  isVerifying.value = true
  verifyError.value = ''
  try {
    await axios.post(`${API_BASE}/verify-password`, { password: currentPassword.value }, { withCredentials: true })
    isVerified.value = true
  } catch (error) {
    isVerified.value = false
    verifyError.value = '비밀번호가 일치하지 않습니다'
  } finally {
    isVerifying.value = false
  }
}

/********************* 2. 비밀번호 변경 *********************/
const saveError = ref('')
const saveSuccess = ref('')
const newPassword = ref('')
const confirmPassword = ref('')

// 공백 방지, 비밀번호-비밀번호재입력 일치 확인
const isSaveEnabled = computed(() => isVerified.value && newPassword.value.length > 0 && confirmPassword.value.length > 0 && newPassword.value === confirmPassword.value)

// 기존 비밀번호 -> 새 비밀번호 변경
const saveNewPassword = async () => {
  saveError.value = ''
  saveSuccess.value = ''
  try {
    await axios.post(`${API_BASE}/change-password`, { newPassword: newPassword.value }, { withCredentials: true })
    saveSuccess.value = '비밀번호가 성공적으로 변경되었습니다'
    currentPassword.value = ''
    newPassword.value = ''
    confirmPassword.value = ''
    isVerified.value = false
    setTimeout(() => {
      router.replace('/about')
    }, 1500)
  } catch (error) {
    saveError.value = '비밀번호 변경에 실패했습니다'
  }
}
/********************* 3. 회원 탈퇴 *********************/
const withdrawError = ref('')
const withdrawSuccess = ref('')
const isWithdrawEnabled = computed(() => isVerified.value)
const withdraw = async () => {
  withdrawError.value = ''
  withdrawSuccess.value = ''
  if (!isWithdrawEnabled.value) return
  if (!confirm('정말로 탈퇴하시겠습니까? 모든 주문 내역이 삭제됩니다')) return
  try {
    await axios.post(`${API_BASE}/withdraw`, {}, { withCredentials: true })
    withdrawSuccess.value = '회원 탈퇴가 완료되었습니다'
    setTimeout(() => {
      router.replace('/')
    }, 1500)
  } catch (e) {
    withdrawError.value = '회원 탈퇴에 실패했습니다'
  }
}
</script>

<template>
  <Header />

  <section class="mx-10 pt-10 md:flex md:flex-col md:justify-center md:items-center md:pt-50">
    <nav>
      <!-- 제목 -->
      <h1 class="text-lg md:text-2xl">회원 설정</h1>

      <!-- 비밀번호 인증 -->
      <div class="flex gap-2 items-center pb-2 pt-3 md:pt-5">
        <input v-model="currentPassword" required type="password" placeholder="비밀번호를 입력하세요" class="p-2 md:p-3 border rounded focus:outline-none focus:border-blue-500" />
        <button class="p-2 md:p-3 ml-2 border rounded focus:outline-none" :class="isVerified ? 'bg-green-500 text-white border-green-700' : 'bg-white border-blue-500 text-blue-700'" @click="verifyPassword" :disabled="isVerifying">인증</button>
      </div>

      <!-- 인증 결과 -->
      <div class="mb-3">
        <span v-if="isVerified" class="text-green-600">인증 성공</span>
        <span v-else="verifyError" class="text-red-600">{{ verifyError }}</span>
      </div>

      <!-- 변경 비밀번호 입력 -->
      <div class="md:flex md:flex-col md:gap-3">
        <input v-model="newPassword" required type="password" placeholder="새 비밀번호" class="p-2 md:p-3 border rounded focus:outline-none focus:border-blue-500 mb-2" />
        <input v-model="confirmPassword" required type="password" placeholder="새 비밀번호 재입력" class="p-2 md:p-3 border rounded focus:outline-none focus:border-blue-500 mb-3" />
      </div>

      <!-- 저장/탈퇴 버튼 -->
      <div class="md:pt-3">
        <button :disabled="!isSaveEnabled" @click="saveNewPassword" :class="['px-5 py-2 md:py-3 md:px-7 rounded mr-2 font-bold transition-colors duration-200', isSaveEnabled ? 'bg-blue-400 text-white cursor-pointer hover:bg-blue-500 border-1 border-black' : 'bg-gray-300 text-gray-500 cursor-not-allowed']">저장</button>
        <button :disabled="!isWithdrawEnabled" @click="withdraw" :class="['px-5 py-2 md:py-3 md:px-7 rounded font-bold transition-colors duration-200 ml-1', isWithdrawEnabled ? 'bg-red-400 text-white cursor-pointer hover:bg-red-500 border-1 border-black' : 'bg-gray-300 text-gray-500 cursor-not-allowed']">탈퇴</button>
      </div>

      <!-- 저장/탈퇴 안내문 -->
      <div v-if="saveSuccess" class="text-green-600 mt-2">{{ saveSuccess }}</div>
      <div v-if="saveError" class="text-red-600 mt-2">{{ saveError }}</div>
      <div v-if="withdrawSuccess" class="text-green-600 mt-2">{{ withdrawSuccess }}</div>
      <div v-if="withdrawError" class="text-red-600 mt-2">{{ withdrawError }}</div>
    </nav>
  </section>
</template>
