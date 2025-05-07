<script setup>
import Footer from '@/components/Footer.vue'
import Header from '@/components/Header.vue'
import axios from 'axios'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const userEmail = ref('')
const userName = ref('')
const password = ref('')
const router = useRouter()

const SignUp = async () => {
  if (!userEmail.value.trim() || !userName.value.trim() || !password.value.trim()) {
    alert('이메일, 학번, 비밀번호는 필수 입력 항목입니다.')
    return
  }
  if (!userEmail.value.includes('@')) {
    alert('유효한 이메일 주소를 입력해주세요.')
    return
  }

  try {
    const response = await axios.post('http://localhost:8080/api/register', {
      userEmail: userEmail.value,
      userName: userName.value,
      password: password.value,
      role: 'student',
    })
    console.log('success :', response.data)
    alert(response.data.message)
    router.push('/login')
  } catch (error) {
    console.error('error :', error.response?.data || error.message)
    alert(error.response?.data?.message || '회원가입에 실패했습니다.')
  }
}
</script>

<template>
  <Header />

  <!-- 로그인 박스 -->
  <section class="text-gray-800 flex flex-col justify-center items-center px-100 py-20">
    <h1 class="text-2xl">KitGet에 처음오셨군요 환영합니다!</h1>

    <!-- 회원가입 -->
    <section class="loginBox">
      <div class="outLine">
        <h1 class="h1Text">Sign up</h1>
        <h2 class="h2Text">Email</h2>
        <input class="inputBox" type="text" v-model="userEmail" required />
        <h2 class="h2Text">학번</h2>
        <input class="inputBox" type="text" v-model="userName" required />
        <h2 class="h2Text">비밀번호</h2>
        <input class="inputBox" type="password" v-model="password" required />
        <h3 class="h3Text">계정이 있으신가요? <router-link to="/login" class="linkLogin">로그인</router-link></h3>
        <button @click="SignUp" class="submitBtn bg-blue-600 hover:bg-blue-700">Submit</button>
      </div>
    </section>
  </section>

  <Footer />
</template>

<style scoped>
@import 'tailwindcss';

.loginBox {
  @apply flex justify-center items-center h-5/6 mt-5;
}
.outLine {
  @apply h-120 w-96 rounded-lg ring-1;
}
.h1Text {
  @apply text-2xl pt-8 pl-8;
}
.h2Text {
  @apply text-gray-500 pt-5 pl-8;
}
.h3Text {
  @apply text-gray-500 text-right pt-6 pr-6;
}
.inputBox {
  @apply w-80 p-1 mt-3 mx-8 rounded-md ring-1 hover:ring-blue-400;
}
.linkLogin {
  @apply hover:text-blue-700 pl-1 pr-1;
}
.submitBtn {
  @apply w-80 mt-5 mx-8 p-2 text-white rounded-lg ring-1;
}
</style>
