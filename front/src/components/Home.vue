<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()
const errorMessage = ref('')

/********************* 1. 로그인/회원가입 탭 전환 (기본 1, 로그인) *********************/
const selectedButton = ref(1)
const selectButton = (button) => {
  selectedButton.value = button
}

/********************************* 2. 회원가입 ***************************************/
// 2-1. 실명인증 팝업 (기본 닫기)
const isPopupOpen = ref(false)
const openVerification = () => {
  isPopupOpen.value = true
}
// 실명인증 드랍다운 메뉴
const schools = [
  { name: '달천초등학교', value: '달천초' },
  { name: '탄금중학교', value: '탄금중' },
  { name: '국원고등학교', value: '국원고' },
  { name: '한국교통대학교', value: '교통대' },
]
const auths = [
  { name: '교사/교수', value: 'teacher' },
  { name: '학생', value: 'student' },
]

// 2-2. 실명인증 완료 여부 (false)
const isVerified = ref(false)
const selectedSchool = ref('')
const name = ref('')
const code = ref('')
const auth = ref('')
const submitVerification = async () => {
  // 학교, 신분, 이름, 학번 공백 체크
  if (!selectedSchool.value || !name.value || !code.value || !auth.value) {
    errorMessage.value = '모든 필드를 입력해주세요!'
    return
  }

  // 학교, 신분, 이름, 학번 일치 확인
  try {
    const response = await axios.post('https://tender-hally-imuisoon-0eaa6cd0.koyeb.app/api/verify', {
      school: selectedSchool.value,
      name: name.value,
      code: code.value,
    })

    // 인증 되면 창 닫고 허가
    if (response.data.verified) {
      isVerified.value = true
      isPopupOpen.value = false
      errorMessage.value = ''
    } else {
      errorMessage.value = '입력한 정보가 일치하지 않습니다'
    }
  } catch (error) {
    errorMessage.value = '서버 오류가 발생했습니다'
  }
}

// 2.3. 회원정보 입력 (이메일, 비밀번호)
const email = ref('')
const password = ref('')
const submitSignUp = async (event) => {
  // 실명인증 없을 때 가입 차단
  event.preventDefault()
  if (!isVerified.value) {
    errorMessage.value = '실명인증을 완료해주세요!'
    return
  }
  // 공백 입력 차단
  if (!email.value || !password.value) {
    errorMessage.value = '이메일과 비밀번호를 입력해주세요!'
    return
  }

  // 가입정보 백엔드 전송
  try {
    const response = await axios.post('https://tender-hally-imuisoon-0eaa6cd0.koyeb.app/api/signup', {
      email: email.value,
      password: password.value,
      school: selectedSchool.value,
      name: name.value,
      code: code.value,
      auth: auth.value,
    })
    // 성공 시 멘트와 0.7초후 새로고침(로그인 이동)
    errorMessage.value = '회원가입 성공!'
    setTimeout(() => {
      window.location.reload()
    }, 700)
  } catch (error) {
    // 백엔드 검토, 사용중인 경우 반려
    errorMessage.value = '이미 사용중인 이메일입니다'
  }
}

/********************************* 3. 로그인 ***************************************/
const isLoggedIn = ref(false)
const submitLogin = async (event) => {
  // 공백 입력 차단
  event.preventDefault()
  if (!email.value || !password.value) {
    errorMessage.value = '이메일과 비밀번호를 입력해주세요!'
    return
  }

  // 회원 검토 백엔드 전송
  try {
    const response = await axios.post(
      'https://tender-hally-imuisoon-0eaa6cd0.koyeb.app/api/login',
      {
        email: email.value,
        password: password.value,
      },
      { withCredentials: true },
    )
    // 로그인 성공 시 메인 페이지(about) 이동
    errorMessage.value = ''
    isLoggedIn.value = true
    router.push('/about')
  } catch (error) {
    console.error('로그인 에러:', error.response?.status, error.response?.data, error.message)
    errorMessage.value = error.response?.data?.message || '로그인 중 오류가 발생했습니다.'
  }
}

/********************************* 4. 로그아웃 ***************************************/
const submitLogout = async () => {
  try {
    const response = await axios.post('https://tender-hally-imuisoon-0eaa6cd0.koyeb.app/api/logout', {}, { withCredentials: true })
    isLoggedIn.value = false
    errorMessage.value = '로그아웃 되었습니다'
    // 로그아웃하면 다시 로그인 화면 이동
    router.push('/')
  } catch (error) {
    console.error('로그아웃 에러:', error.response?.status, error.response?.data, error.message)
    errorMessage.value = '로그아웃 중 오류가 발생했습니다.'
  }
}
</script>

<template>
  <section class="flex flex-col items-center mt-20">
    <!-- 1. 로고 -->
    <h1 class="font-[LogoFont] md:text-6xl text-4xl text-blue-400 select-none">KitGet</h1>

    <!-- 2. 선택 버튼 -->
    <div class="mt-3 md:mt-10 mb-5 md:mb-10">
      <button @click="selectButton(1)" :class="['md:text-xl px-4 py-2 border-b-1 select-none', selectedButton === 1 ? 'border-blue-500' : 'border-transparent']">로그인</button>
      <button @click="selectButton(2)" :class="['md:text-xl px-4 py-2 border-b-1 select-none', selectedButton === 2 ? 'border-blue-500' : 'border-transparent']">회원가입</button>
    </div>

    <!-- 3. 로그인 폼 -->
    <div v-if="selectedButton === 1" class="w-4/5 md:w-3/5">
      <form class="flex flex-col gap-4 md:gap-6" @submit.prevent="submitLogin">
        <input v-model="email" required type="email" placeholder="Email을 입력하세요" class="md:p-4 p-2 border rounded focus:outline-none focus:border-blue-500" />
        <input v-model="password" required type="password" placeholder="비밀번호를 입력하세요" class="md:p-4 p-2 border rounded focus:outline-none focus:border-blue-500" />
        <div v-if="errorMessage" class="text-red-500 text-sm">{{ errorMessage }}</div>
        <button type="submit" class="p-2 md:p-4 bg-blue-500 hover:bg-blue-600 text-white rounded">로그인</button>
      </form>
    </div>

    <!-- 4. 회원가입 폼 -->
    <div v-if="selectedButton === 2" class="w-4/5 md:w-3/5">
      <button @click="openVerification" :class="['mb-4 md:p-4 p-2 border rounded w-full', isVerified ? 'bg-green-300 cursor-not-allowed border-green-400' : 'bg-blue-500 hover:bg-blue-600 text-white']" :disabled="isVerified">실명인증</button>
      <form class="flex flex-col gap-4 md:gap-6" @submit.prevent="submitSignUp">
        <input v-model="email" required type="email" placeholder="Email" class="md:p-4 p-2 border rounded focus:outline-none focus:border-blue-500" />
        <input v-model="password" required type="password" placeholder="비밀번호" class="md:p-4 p-2 border rounded focus:outline-none focus:border-blue-500" />
        <div v-if="errorMessage" class="text-red-500 text-sm">{{ errorMessage }}</div>
        <button type="submit" :class="['p-2 md:p-4 text-white rounded', isVerified ? 'bg-blue-500 hover:bg-blue-600' : 'bg-gray-400 cursor-not-allowed']" :disabled="!isVerified">가입하기</button>
      </form>
    </div>

    <!-- 5. 실명인증 팝업 -->
    <div v-if="isPopupOpen" class="fixed inset-0 bg-black/50 flex items-center justify-center">
      <div class="bg-white p-6 md:p-10 rounded-lg w-80 md:w-100">
        <h2 class="text-xl md:text-3xl font-bold mb-4 md:mb-10">실명인증</h2>
        <form class="flex flex-col gap-4 md:gap-7" @submit.prevent="submitVerification">
          <select v-model="selectedSchool" class="p-2 md:p-3 border rounded focus:outline-none focus:border-blue-500" required>
            <option value="" disabled>학교를 선택해주세요</option>
            <option v-for="school in schools" :key="school.value" :value="school.value">
              {{ school.name }}
            </option>
          </select>
          <select v-model="auth" class="p-2 md:p-3 border rounded focus:outline-none focus:border-blue-500" required>
            <option value="" disabled>신분을 선택해주세요</option>
            <option v-for="auth in auths" :key="auth.value" :value="auth.value">
              {{ auth.name }}
            </option>
          </select>
          <input v-model="name" type="text" placeholder="이름" class="p-2 md:p-3 border rounded focus:outline-none focus:border-blue-500" required />
          <input v-model="code" type="text" placeholder="학번" class="p-2 md:p-3 border rounded focus:outline-none focus:border-blue-500" required />
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
