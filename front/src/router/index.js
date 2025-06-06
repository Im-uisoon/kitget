import { createRouter, createWebHashHistory } from 'vue-router'
import axios from 'axios'
import Home from '../components/Home.vue'
import SchoolPage from '../components/SchoolPage.vue'
import SchoolStore from '@/components/SchoolStore.vue'
import Content from '@/components/Notice/Content.vue'

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
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
      path: '/settings',
      name: 'settings',
      component: () => import('../components/Settings.vue'),
    },
    {
      path: '/todo',
      name: 'todo',
      component: () => import('../components/Todo.vue'),
    },
    {
      path: '/notice',
      name: 'notice',
      component: () => import('../components/Notice/Notice.vue'),
    },
    {
      path: '/notice/write',
      name: 'write',
      component: () => import('../components/Notice/Write.vue'),
    },
    {
      path: '/school/:schoolCode',
      name: 'SchoolPage',
      component: SchoolPage,
    },
    {
      path: '/store/:schoolCode',
      name: 'SchoolStore',
      component: SchoolStore,
    },
    {
      path: '/notice/:id',
      name: 'Content',
      component: Content,
    },
  ],
})

router.beforeEach(async (to, from, next) => {
  if (to.name === 'SchoolPage') {
    try {
      const schoolCode = to.params.schoolCode
      const response = await axios.get(`https://tender-hally-imuisoon-0eaa6cd0.koyeb.app/api/school/${schoolCode}`, { withCredentials: true })
      if (response.data.isAccess) {
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
