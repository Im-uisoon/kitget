import { createRouter, createWebHistory } from 'vue-router'
import axios from 'axios'
import Home from '../components/Home.vue'
import SchoolPage from '../components/SchoolPage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../components/About.vue'),
    },
    {
      path: '/order',
      name: 'order',
      component: () => import('../components/Order.vue'),
    },
    {
      path: '/school:schoolCode',
      name: 'SchoolPage',
      component: SchoolPage,
    },
  ],
})

router.beforeEach(async (to, from, next) => {
  if (to.name === 'SchoolPage') {
    try {
      const schoolCode = to.params.schoolCode
      const response = await axios.get(`http://localhost:8080/api/school/${schoolCode}`, { withCredentials: true })
      if (response.data.allowed) {
        next()
      } else {
        next('/about')
      }
    } catch (error) {
      next('/about')
    }
  } else {
    next()
  }
})

export default router
