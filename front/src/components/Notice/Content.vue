<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'
import Header from '../view/Header.vue'

const route = useRoute()
const router = useRouter()
const notice = ref(null)
const userAuth = ref(null)
const userEmail = ref(null)

const fetchMyInfo = async () => {
  try {
    const res = await axios.get('https://tender-hally-imuisoon-0eaa6cd0.koyeb.app/api/me', { withCredentials: true })
    userAuth.value = res.data.auth
    userEmail.value = res.data.email
  } catch (e) {
    userAuth.value = null
    userEmail.value = null
  }
}

onMounted(async () => {
  await fetchMyInfo()
  const res = await axios.get(`https://tender-hally-imuisoon-0eaa6cd0.koyeb.app/api/notice/${route.params.id}`, { withCredentials: true })
  notice.value = res.data
})

const deleteNotice = async () => {
  if (confirm('정말 삭제하시겠습니까?')) {
    await axios.delete(`https://tender-hally-imuisoon-0eaa6cd0.koyeb.app/api/notice/${notice.value.id}`, { withCredentials: true })
    router.push('/notice')
  }
}

// 뒤로가기(목록으로)
const goBack = () => {
  router.push('/notice')
}
</script>

<template>
  <Header />
  <section class="pt-10 mx-10 xl:mx-25 xl:pt-20">
    <div class="flex items-center mb-3 xl:mb-10">
      <button @click="goBack" class="flex items-center text-blue-600 font-medium"><span class="text-xl xl:text-4xl mr-5">←</span></button>
      <h2 class="text-2xl xl:text-4xl font-bold text-gray-800 flex-1 text-left">{{ notice?.title || '' }}</h2>
    </div>
    <div v-if="notice">
      <div class="flex items-center justify-between mb-4 xl:mb-10">
        <div class="text-sm xl:text-xl text-gray-500">
          작성자: <span class="font-semibold">{{ notice.writerName }}</span>
          <span class="mx-2">|</span>
          <span>{{ new Date(notice.createdAt).toLocaleDateString() }}</span>
        </div>
        <button v-if="userAuth === 'teacher' && notice.writerEmail === userEmail" @click="deleteNotice" class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 transition">삭제</button>
      </div>
      <hr class="mb-6 xl:mb-10" />
      <div class="text-gray-800 text-lg xl:text-4xl whitespace-pre-line min-h-[120px] xl:mx-10">
        {{ notice.content }}
      </div>
    </div>
    <div v-else class="text-center text-gray-500 py-10">공지사항을 불러오는 중입니다...</div>
  </section>
</template>
