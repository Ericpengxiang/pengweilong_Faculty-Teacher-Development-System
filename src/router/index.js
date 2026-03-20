import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: () => import('@/views/Login.vue'), meta: { title: '登录' } },
  { path: '/register', component: () => import('@/views/Register.vue'), meta: { title: '注册' } },
  {
    path: '/home',
    component: () => import('@/views/Layout.vue'),
    redirect: '/home/index',
    children: [
      { path: 'index', component: () => import('@/views/Index.vue'), meta: { title: '首页' } },
      { path: 'archives', component: () => import('@/views/ArchiveList.vue'), meta: { title: '档案管理' } },
      { path: 'archive/add', component: () => import('@/views/ArchiveForm.vue'), meta: { title: '新增档案' } },
      { path: 'archive/edit/:id', component: () => import('@/views/ArchiveForm.vue'), meta: { title: '编辑档案' } },
      { path: 'archive/detail/:id', component: () => import('@/views/ArchiveDetail.vue'), meta: { title: '档案详情' } },
      { path: 'statistics', component: () => import('@/views/Statistics.vue'), meta: { title: '统计分析' } },
      { path: 'profile', component: () => import('@/views/Profile.vue'), meta: { title: '个人中心' } },
    ]
  },
  { path: '*', redirect: '/login' }
]

const router = new VueRouter({
  mode: 'history',
  base: '/',
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  document.title = (to.meta.title ? to.meta.title + ' - ' : '') + '高校教师成长档案管理系统'
  const token = localStorage.getItem('token')
  if (to.path === '/login' || to.path === '/register') {
    if (token) {
      next('/home/index')
    } else {
      next()
    }
  } else {
    if (!token) {
      next('/login')
    } else {
      next()
    }
  }
})

export default router
