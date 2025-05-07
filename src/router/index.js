import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import Resell from '@/views/Resell.vue'
import Review from '@/views/Review.vue'
import Notice from '@/views/Notice.vue'
import Faq from '@/views/Faq.vue'
import Content from '@/components/Community/Content.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/Member/Register.vue'),
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/Member/Login.vue'),
    },
    {
      path: '/intro',
      name: 'intro',
      component: () => import('../views/Document/Intro.vue'),
    },
    {
      path: '/policy',
      name: 'policy',
      component: () => import('../views/Document/Policy.vue'),
    },
    {
      path: '/privacy',
      name: 'privacy',
      component: () => import('../views/Document/Privacy.vue'),
    },
    {
      path: '/post',
      name: 'post',
      component: () => import('../components/Community/Post.vue'),
    },
    {
      path: '/admin/post',
      name: 'AdminPost',
      meta: { requiresAdmin: true },
      component: () => import('../components/Community/AdminPost.vue'),
    },
    {
      path: '/resell',
      redirect: '/resell/1',
    },
    {
      path: '/resell/:page',
      name: 'resell',
      component: Resell,
    },
    {
      path: '/review',
      redirect: '/review/1',
    },
    {
      path: '/review/:page',
      name: 'review',
      component: Review,
    },
    {
      path: '/faq',
      redirect: '/faq/1',
    },
    {
      path: '/faq/:page',
      name: 'faq',
      component: Faq,
    },
    {
      path: '/notice',
      redirect: '/notice/1',
    },
    {
      path: '/notice/:page',
      name: 'notice',
      component: Notice,
    },
    {
      path: '/content/:page',
      name: 'content',
      component: Content,
    },
    {
      path: '/store',
      name: 'store',
      component: () => import('../views/Store.vue'),
    },
    {
      path: '/shopping',
      name: 'shopping',
      component: () => import('../views/Shopping.vue'),
    },
    {
      path: '/letter',
      name: 'letter',
      component: () => import('../views/Letter.vue'),
    },
  ],
})

router.beforeEach(async (to, from, next) => {
  if (to.meta.requiresAdmin) {
    const response = await fetch('http://localhost:8080/api/session-check', { credentials: 'include' })
    if (response.status === 200) {
      const data = await response.json()
      if (data.isAuthenticated && data.authorities?.includes('ROLE_ADMIN')) {
        next()
      } else {
        next('/login')
      }
    } else {
      next('/login')
    }
  } else {
    next()
  }
})

export default router
