<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import Header from './view/Header.vue'

/********************************* 1. todo 로딩 ***************************************/
const todos = ref([])
const fetchTodos = async () => {
  const res = await axios.get('tender-hally-imuisoon-0eaa6cd0.koyeb.app/api/todo', { withCredentials: true })
  todos.value = res.data
}

/********************************* 2. todo 추가 ***************************************/
const newTodo = ref('')
const addTodo = async () => {
  if (!newTodo.value.trim()) return
  await axios.post('tender-hally-imuisoon-0eaa6cd0.koyeb.app/api/todo', { content: newTodo.value }, { withCredentials: true })
  newTodo.value = ''
  fetchTodos()
}

/********************************* 3. todo 삭제 ***************************************/
const deleteTodo = async (id) => {
  await axios.delete(`tender-hally-imuisoon-0eaa6cd0.koyeb.app/api/todo/${id}`, { withCredentials: true })
  fetchTodos()
}

/********************************* 4. todo 체크 ***************************************/
const toggleCompleted = async (todo) => {
  await axios.patch(`tender-hally-imuisoon-0eaa6cd0.koyeb.app/api/todo/${todo.id}/completed`, { completed: !todo.completed }, { withCredentials: true })
  fetchTodos()
}

onMounted(fetchTodos)
</script>

<template>
  <Header />
  <section class="mx-10">
    <div class="max-w-md mx-auto mt-10 md:mt-30">
      <h1 class="text-xl md:text-4xl font-bold mb-4 md:mb-10">나의 To-Do List</h1>
      <div class="flex mb-4 md:mb-10">
        <input v-model="newTodo" @keyup.enter="addTodo" class="border rounded p-2 md:p-3 flex-1 focus:outline-none focus:border-blue-500 select-none" placeholder="할 일을 입력하세요" />
        <button @click="addTodo" class="ml-2 px-4 py-2 bg-blue-500 hover:bg-blue-400 text-white rounded select-none">추가</button>
      </div>
      <ul class="mb-5">
        <li v-for="todo in todos" :key="todo.id" class="flex justify-between items-center border-b border-blue-400 py-2 md:py-4 md:text-xl">
          <div class="flex items-center gap-2">
            <button @click="toggleCompleted(todo)" class="text-xl focus:outline-none">
              <span v-if="todo.completed" class="text-gray-600 pl-2 select-none">🗑️</span>
              <span v-else class="text-green-600 pl-2 select-none">✅</span>
            </button>
            <span :class="{ 'line-through text-gray-400': todo.completed }">{{ todo.content }}</span>
          </div>
          <button @click="deleteTodo(todo.id)" class="text-red-500 pr-3 select-none">X</button>
        </li>
      </ul>
    </div>
  </section>
</template>
