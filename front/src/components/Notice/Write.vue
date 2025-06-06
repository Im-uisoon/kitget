<script setup>
import { ref, computed } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import Header from '../view/Header.vue'

const router = useRouter()
const title = ref('')
const content = ref('')
const error = ref('')

const isEnabled = computed(() => title.value.trim().length > 0 && content.value.trim().length > 0)

const submit = async () => {
  error.value = ''
  if (!isEnabled.value) return
  try {
    await axios.post('tender-hally-imuisoon-0eaa6cd0.koyeb.app/api/notice', { title: title.value, content: content.value }, { withCredentials: true })
    router.push('/notice')
  } catch (e) {
    error.value = '글 등록에 실패했습니다. (권한이 없거나 세션이 만료되었습니다)'
  }
}

// 뒤로가기(목록으로)
const goBack = () => {
  router.push('/notice')
}
</script>

<template>
  <Header />
  <section class="mx-10">
    <div class="max-w-lg mx-auto mt-10">
      <div class="flex items-center mb-3">
        <button @click="goBack" class="text-blue-600 font-medium"><span class="text-xl mr-5">←</span></button>
        <h2 class="text-xl font-bold">공지 작성</h2>
      </div>
      <input v-model="title" placeholder="제목" class="border rounded px-3 py-2 w-full mb-3" maxlength="100" />
      <textarea v-model="content" placeholder="내용" class="border rounded px-3 py-2 w-full mb-3 min-h-[120px]" maxlength="2000"></textarea>
      <button @click="submit" :disabled="!isEnabled" :class="['px-5 py-2 rounded font-bold transition-colors duration-200', isEnabled ? 'bg-blue-500 text-white cursor-pointer hover:bg-blue-600' : 'bg-gray-300 text-gray-500 cursor-not-allowed']">등록</button>
      <div v-if="error" class="text-red-600 mt-2">{{ error }}</div>
    </div>
  </section>
</template>
