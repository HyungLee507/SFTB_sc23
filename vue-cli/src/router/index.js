import Vue from 'vue';
import Router from 'vue-router';
import LoginPage from '@/components/LoginPage';
import RegisterPage from '@/components/RegisterPage';
import FindPassword from '@/components/FindPassword';
import ProductregistrationPage from '@/components/ProductregistrationPage';
import ProductinformationPage from '@/components/ProductinformationPage';
import ProductlistPage from '@/components/ProductlistPage';
import UserInputPage from '@/components/UserInputPage';

Vue.use(Router);

const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/account/login',
      component: LoginPage,
      name: 'login',
    },
    {
      path: '/account/join',
      component: RegisterPage,
    },
    {
      path: '/account/findpassword',
      component: FindPassword,
    },
    {
      path: '/product/registration',
      component: ProductregistrationPage,
    },
    {
      path: '/product/list/:id',
      component: ProductinformationPage
    },
    {
      path: '/product/list',
      component: ProductlistPage,
    },
    {
      path: '/user/input',
      component: UserInputPage,
    },
  ]
});

router.beforeEach((to, from, next) => {
  const isLoggedIn = !!localStorage.getItem('accessToken');
  if (isLoggedIn && (to.path === '/account/login' || to.path === '/account/join')) {
    next('/'); // 로그인된 사용자가 '/account/login' 또는 '/account/join'에 접근하려고 하면 루트 경로('/')로 리다이렉트
  } else {
    next(); // 그 외의 경우에는 요청된 경로로 진행
  }
});

export default router;