<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'

// 쪽지 데이터 및 입력값
const letters = ref([])
const receiverId = ref('')
const content = ref('')

// 받은 쪽지 조회
const fetchLetters = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/letter', {
      withCredentials: true,
    })
    letters.value = response.data
  } catch (error) {
    console.error('쪽지 조회 실패:', error)
    if (error.response?.status === 401) {
      alert('로그인이 필요합니다.')
      window.location.href = '/login'
    } else {
      alert('로그인이 필요합니다.')
      window.location.href = '/login'
    }
  }
}

// 쪽지 보내기
const sendLetter = async () => {
  if (!receiverId.value || !content.value) {
    alert('수신자와 내용을 입력해주세요.')
    return
  }
  try {
    await axios.post(
      'http://localhost:8080/api/letter',
      {
        receiverId: receiverId.value,
        content: content.value,
      },
      {
        withCredentials: true,
      },
    )
    alert('쪽지가 전송되었습니다.')
    receiverId.value = ''
    content.value = ''
    fetchLetters() // 목록 갱신
  } catch (error) {
    console.error('쪽지 전송 실패:', error)
    alert('쪽지 전송에 실패했습니다.')
  }
}

// 쪽지 삭제
const deleteLetter = async (id) => {
  if (!confirm('이 쪽지를 삭제하시겠습니까?')) return
  try {
    await axios.delete(`http://localhost:8080/api/letter/${id}`, {
      withCredentials: true,
    })
    letters.value = letters.value.filter((letter) => letter.id !== id)
    alert('쪽지가 삭제되었습니다.')
  } catch (error) {
    console.error('삭제 실패:', error)
    alert('쪽지 삭제에 실패했습니다.')
  }
}

// 컴포넌트 마운트 시 쪽지 가져오기
onMounted(() => {
  fetchLetters()
})
</script>

<template>
  <Header />
  <div class="flex flex-col items-center justify-center px-30">
    <section class="w-full h-full flex mt-10">
      <div class="w-4/5 mx-auto">
        <h2 class="text-2xl font-bold mb-4">쪽지함</h2>

        <!-- 쪽지 보내기 폼 -->
        <div class="mb-6 p-4 bg-gray-100 rounded-lg">
          <h3 class="text-lg font-semibold mb-2">쪽지 보내기</h3>

          <div class="flex flex-col gap-2">
            <input v-model="receiverId" type="text" placeholder="받는 사람 ID" class="p-2 border rounded-lg" />
            <i class="text-gray-500">운영자 메일은 admin@kitget.com 입니다</i>
            <textarea v-model="content" placeholder="내용을 입력하세요" class="p-2 border rounded-lg h-24"></textarea>
            <button @click="sendLetter" class="p-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">보내기</button>
          </div>
        </div>

        <!-- 받은 쪽지 목록 -->
        <ul v-if="letters.length > 0" class="space-y-4">
          <li v-for="letter in letters" :key="letter.id" class="p-4 bg-gray-100 rounded-lg">
            <div class="flex justify-between items-center">
              <span class="font-semibold">보낸 사람: {{ letter.senderId }}</span>
              <button @click="deleteLetter(letter.id)" class="text-red-500 hover:text-red-700 font-bold">X</button>
            </div>
            <p class="mt-2">{{ letter.content }}</p>
          </li>
        </ul>
        <p v-else class="text-gray-500">받은 쪽지가 없습니다.</p>
      </div>
    </section>
  </div>
  <Footer />
</template>

<style scoped>
@import 'tailwindcss';
</style>
