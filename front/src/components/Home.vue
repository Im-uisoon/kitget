<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()
const selectedButton = ref(1)
const isVerified = ref(false)
const isPopupOpen = ref(false)
const selectedSchool = ref('')
const name = ref('')
const code = ref('')
const auth = ref('')
const email = ref('')
const password = ref('')
const errorMessage = ref('')
const isLoggedIn = ref(false)

const selectButton = (button) => {
  selectedButton.value = button
}

const openVerification = () => {
  isPopupOpen.value = true
}

const submitVerification = async () => {
  if (!selectedSchool.value || !name.value || !code.value || !auth.value) {
    errorMessage.value = '모든 필드를 입력해주세요!'
    return
  }

  try {
    const response = await axios.post('http://localhost:8080/api/verify', {
      school: selectedSchool.value,
      name: name.value,
      code: code.value,
    })

    if (response.data.verified) {
      isVerified.value = true
      isPopupOpen.value = false
      errorMessage.value = ''
    } else {
      errorMessage.value = '입력한 정보가 일치하지 않습니다.'
    }
  } catch (error) {
    errorMessage.value = '서버 오류가 발생했습니다.'
  }
}

const submitSignUp = async (event) => {
  event.preventDefault()
  if (!isVerified.value) {
    errorMessage.value = '실명인증을 완료해주세요!'
    return
  }
  if (!email.value || !password.value) {
    errorMessage.value = '이메일과 비밀번호를 입력해주세요!'
    return
  }

  try {
    const response = await axios.post('http://localhost:8080/api/signup', {
      email: email.value,
      password: password.value,
      school: selectedSchool.value,
      name: name.value,
      code: code.value,
      auth: auth.value,
    })
    errorMessage.value = '회원가입 성공!'
    setTimeout(() => {
      window.location.reload()
    }, 1000)
  } catch (error) {
    errorMessage.value = '이미 사용중인 이메일입니다'
  }
}

const submitLogin = async (event) => {
  event.preventDefault()
  if (!email.value || !password.value) {
    errorMessage.value = '이메일과 비밀번호를 입력해주세요!'
    return
  }

  try {
    const response = await axios.post(
      'http://localhost:8080/api/login',
      {
        email: email.value,
        password: password.value,
      },
      { withCredentials: true },
    )
    errorMessage.value = ''
    isLoggedIn.value = true
    router.push('/about')
  } catch (error) {
    console.error('로그인 에러:', error.response?.status, error.response?.data, error.message)
    errorMessage.value = error.response?.data?.message || '로그인 중 오류가 발생했습니다.'
  }
}

const submitLogout = async () => {
  try {
    const response = await axios.post('http://localhost:8080/api/logout', {}, { withCredentials: true })
    isLoggedIn.value = false
    errorMessage.value = '로그아웃 되었습니다'
    router.push('/')
  } catch (error) {
    console.error('로그아웃 에러:', error.response?.status, error.response?.data, error.message)
    errorMessage.value = '로그아웃 중 오류가 발생했습니다.'
  }
}

const schools = [
  { name: '달천초등학교', value: '001' },
  { name: '탄금중학교', value: '002' },
  { name: '국원고등학교', value: '003' },
  { name: '한국교통대학교', value: '004' },
]
const auths = [
  { name: '교사/교수', value: 'teacher' },
  { name: '학생', value: 'student' },
]
</script>

<template>
  <section class="flex flex-col items-center mt-20">
    <!-- 1. 로고 -->
    <h1 class="font-[LogoFont] text-4xl text-blue-400 select-none">KitGet</h1>

    <!-- 2. 선택 버튼 -->
    <div class="mt-3 mb-5">
      <button @click="selectButton(1)" :class="['px-4 py-2 border-b-1 select-none', selectedButton === 1 ? 'border-blue-500' : 'border-transparent']">로그인</button>
      <button @click="selectButton(2)" :class="['px-4 py-2 border-b-1 select-none', selectedButton === 2 ? 'border-blue-500' : 'border-transparent']">회원가입</button>
    </div>

    <!-- 3. 로그인 폼 -->
    <div v-if="selectedButton === 1" class="w-4/5">
      <form class="flex flex-col gap-4" @submit.prevent="submitLogin">
        <input v-model="email" required type="email" placeholder="Email을 입력하세요" class="p-2 border rounded focus:outline-none focus:border-blue-500" />
        <input v-model="password" required type="password" placeholder="비밀번호를 입력하세요" class="p-2 border rounded focus:outline-none focus:border-blue-500" />
        <div v-if="errorMessage" class="text-red-500 text-sm">{{ errorMessage }}</div>
        <button type="submit" class="p-2 bg-blue-500 hover:bg-blue-600 text-white rounded">로그인</button>
      </form>
    </div>

    <!-- 4. 회원가입 폼 -->
    <div v-if="selectedButton === 2" class="w-4/5">
      <button @click="openVerification" :class="['mb-4 p-2 border rounded w-full', isVerified ? 'bg-green-300 cursor-not-allowed border-green-400' : 'bg-blue-500 hover:bg-blue-600 text-white']" :disabled="isVerified">실명인증</button>
      <form class="flex flex-col gap-4" @submit.prevent="submitSignUp">
        <input v-model="email" required type="email" placeholder="Email" class="p-2 border rounded focus:outline-none focus:border-blue-500" />
        <input v-model="password" required type="password" placeholder="비밀번호" class="p-2 border rounded focus:outline-none focus:border-blue-500" />
        <div v-if="errorMessage" class="text-red-500 text-sm">{{ errorMessage }}</div>
        <button type="submit" :class="['p-2 text-white rounded', isVerified ? 'bg-blue-500 hover:bg-blue-600' : 'bg-gray-400 cursor-not-allowed']" :disabled="!isVerified">가입하기</button>
      </form>
    </div>

    <!-- 5. 실명인증 팝업 -->
    <div v-if="isPopupOpen" class="fixed inset-0 bg-black/50 flex items-center justify-center">
      <div class="bg-white p-6 rounded-lg w-80">
        <h2 class="text-xl font-bold mb-4">실명인증</h2>
        <form class="flex flex-col gap-4" @submit.prevent="submitVerification">
          <select v-model="selectedSchool" class="p-2 border rounded focus:outline-none focus:border-blue-500" required>
            <option value="" disabled>학교를 선택해주세요</option>
            <option v-for="school in schools" :key="school.value" :value="school.value">
              {{ school.name }}
            </option>
          </select>
          <select v-model="auth" class="p-2 border rounded focus:outline-none focus:border-blue-500" required>
            <option value="" disabled>신분을 선택해주세요</option>
            <option v-for="auth in auths" :key="auth.value" :value="auth.value">
              {{ auth.name }}
            </option>
          </select>
          <input v-model="name" type="text" placeholder="이름" class="p-2 border rounded focus:outline-none focus:border-blue-500" required />
          <input v-model="code" type="text" placeholder="학번" class="p-2 border rounded focus:outline-none focus:border-blue-500" required />
          <div v-if="errorMessage" class="text-red-500 text-sm">{{ errorMessage }}</div>
          <div class="flex gap-2 w-full justify-between">
            <button type="submit" class="p-2 bg-blue-500 hover:bg-blue-600 text-white rounded w-full">제출</button>
            <button type="button" @click="isPopupOpen = false" class="p-2 bg-red-400 hover:bg-red-500 text-white rounded w-full">취소</button>
          </div>
        </form>
      </div>
    </div>
  </section>
</template>
