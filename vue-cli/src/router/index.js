import Vue from 'vue';
import Router from 'vue-router';
import LoginPage from '@/components/LoginPage'
import RegisterPage from '@/components/RegisterPage'
import FindPassword from '@/components/FindPassword'
import ProductregistrationPage from '@/components/ProductregistrationPage'
import ProductinformationPage from '@/components/ProductinformationPage'
import ProductlistPage from '@/components/ProductlistPage'
import UserInputPage from "@/components/UserInputPage.vue";
import CartPage from "@/components/CartPage.vue";
import MyPage from "@/components/MyPage.vue";
import ProfilePage from "@/components/ProfilePage.vue";
import VirtualFittingPage from "@/components/VirtualFittingPage.vue";
import OrdersPage from "@/components/OrdersPage.vue";
import SalesHistoryPage from "@/components/SalesHistoryPage.vue";
import SellingPage from "@/components/SellingPage.vue";
import PaymentPage from "@/components/PaymentPage.vue";
import SoldPage from "@/components/SoldPage.vue";
import SellingProductInformationPage from "@/components/SellingProductInformationPage.vue";


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
        {
            path: '/product-list/:id',
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
        {
            path: '/user/cart',
            component: CartPage,
        },
        {
            path: '/user/mypage',
            component: MyPage,
        },
        {
            path: '/user/mypage/profile',
            component: ProfilePage,
        },
        {
            path: '/user/mypage/orders',   //없앨지말지
            component: OrdersPage,
        },
        {
            path: '/user/mypage/virtualfitting',
            component: VirtualFittingPage,
        },
        {
            path: '/user/mypage/saleshistory',
            component: SalesHistoryPage,
        },
        {
            path: '/user/mypage/saleshistory/selling',
            component: SellingPage,
        },
        {
            path: '/user/mypage/saleshistory/sold',
            component: SoldPage,
        },
        {
            path: '/user/mypage/saleshistory/sellingproduct/:productId',
            component: SellingProductInformationPage,
        },
        {
            path: '/product/payment/:id',
            component: PaymentPage,
        }


    ]
});
