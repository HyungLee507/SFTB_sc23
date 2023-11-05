import Vue from 'vue';
import Router from 'vue-router';
import LoginPage from '@/components/LoginPage'
import RegisterPage from '@/components/RegisterPage'
import FindPassword from '@/components/FindPassword'
import ProductregistrationPage from '@/components/ProductregistrationPage'
import ProductinformationPage from '@/components/ProductinformationPage'
import ProductlistPage from '@/components/ProductlistPage'


Vue.use(Router); // VueRouter를 사용하겠다.

export default new Router({
    mode: 'history',
    routes: [
        {
            path: '/account/login',
            component: LoginPage,
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
        { path: '/product/list/:id', 
        component: ProductinformationPage },
        {
            path: '/product/list',
            component: ProductlistPage,
        },
        
        
    ]
});