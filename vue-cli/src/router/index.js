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
import SalesHistoryPage from "@/components/SalesHistoryPage.vue";
import SellingPage from "@/components/SellingPage.vue";
import PaymentPage from "@/components/PaymentPage.vue";
import SoldPage from "@/components/SoldPage.vue";
import SellingProductInformationPage from "@/components/SellingProductInformationPage.vue";
import ReviewPage from "@/components/ReviewPage.vue";
import UploadPage from "@/components/UploadPage.vue";
import TradingPage from "@/components/TradingPage.vue";
import buyerTradingPage from "@/components/buyerTradingPage"
import CommentPage from "@/components/CommentPage.vue";

Vue.use(Router);
export default new Router({
    mode: 'history',
    routes: [
        {
            path: '/account/login',
            component: LoginPage,
                beforeEnter: (to, from, next) => {
                const accessToken = localStorage.getItem('accessToken');
                if (accessToken) {
                    alert('이미 로그인 되어있습니다.')
                    next('/');

                } else {
                    next();
                }
            }
        },
        {
            path: '/account/join',
            component: RegisterPage,
                beforeEnter: (to, from, next) => {
                const accessToken = localStorage.getItem('accessToken');
                if (accessToken) {
                    alert('이미 로그인 되어있습니다.')
                    next('/');

                } else {
                    next();
                }
            }
        },
        {
            path: '/account/findpassword',
            component: FindPassword,
                beforeEnter: (to, from, next) => {
                const accessToken = localStorage.getItem('accessToken');
                if (accessToken) {
                    alert('이미 로그인 되어있습니다.')
                    next('/');

                } else {
                    next();
                }
            }

        },
        {
            path: '/product/registration',
            component: ProductregistrationPage,
                beforeEnter: (to, from, next) => {
                const accessToken = localStorage.getItem('accessToken');
                if (!accessToken) {
                    alert('로그인이 필요합니다.')
                    next('/');

                } else {
                    next();
                }
            }
        },
        {
            path: '/product-list/:id',
            component: ProductinformationPage,
            name: 'productinformationPage',
        },
        {
            path: '/product/list',
            component: ProductlistPage,
        },
        {
            path: '/user/input',
            component: UserInputPage,
                beforeEnter: (to, from, next) => {
                const accessToken = localStorage.getItem('accessToken');
                if (!accessToken) {
                    alert('로그인이 필요합니다.')
                    next('/');

                } else {
                    next();
                }
            }
        },
        {
            path: '/user/cart',
            component: CartPage,
                beforeEnter: (to, from, next) => {
                const accessToken = localStorage.getItem('accessToken');
                if (!accessToken) {
                    alert('로그인이 필요합니다.')
                    next('/');

                } else {
                    next();
                }
            }
        },
        {
            path: '/user/mypage',
            component: MyPage,
                beforeEnter: (to, from, next) => {
                const accessToken = localStorage.getItem('accessToken');
                if (!accessToken) {
                    alert('로그인이 필요합니다.')
                    next('/');

                } else {
                    next();
                }
            }
        },
        {
            path: '/user/mypage/buyertrading',
            component: buyerTradingPage,
                beforeEnter: (to, from, next) => {
                const accessToken = localStorage.getItem('accessToken');
                if (!accessToken) {
                    alert('로그인이 필요합니다.')
                    next('/');

                } else {
                    next();
                }
            }

        },
        {
            path: '/user/mypage/profile',
            component: ProfilePage,
                            beforeEnter: (to, from, next) => {
                const accessToken = localStorage.getItem('accessToken');
                if (!accessToken) {
                    alert('로그인이 필요합니다.')
                    next('/');

                } else {
                    next();
                }
            }
        },
        {
            path: '/user/mypage/virtualfitting',
            component: VirtualFittingPage,
                            beforeEnter: (to, from, next) => {
                const accessToken = localStorage.getItem('accessToken');
                if (!accessToken) {
                    alert('로그인이 필요합니다.')
                    next('/');

                } else {
                    next();
                }
            }
        },
        {
            path: '/user/mypage/saleshistory',
            component: SalesHistoryPage,
                            beforeEnter: (to, from, next) => {
                const accessToken = localStorage.getItem('accessToken');
                if (!accessToken) {
                    alert('로그인이 필요합니다.')
                    next('/');

                } else {
                    next();
                }
            }
        },
        {
            path: '/user/mypage/saleshistory/selling',
            component: SellingPage,
                            beforeEnter: (to, from, next) => {
                const accessToken = localStorage.getItem('accessToken');
                if (!accessToken) {
                    alert('로그인이 필요합니다.')
                    next('/');

                } else {
                    next();
                }
            }
        },
        {
            path: '/user/mypage/saleshistory/trading',
            component: TradingPage,
                            beforeEnter: (to, from, next) => {
                const accessToken = localStorage.getItem('accessToken');
                if (!accessToken) {
                    alert('로그인이 필요합니다.')
                    next('/');

                } else {
                    next();
                }
            }
        },
        {
            path: '/user/mypage/saleshistory/sold',
            component: SoldPage,
                            beforeEnter: (to, from, next) => {
                const accessToken = localStorage.getItem('accessToken');
                if (!accessToken) {
                    alert('로그인이 필요합니다.')
                    next('/');

                } else {
                    next();
                }
            }
        },
        {
            path: '/user/mypage/saleshistory/sellingproduct/:productId',
            component: SellingProductInformationPage,
                            beforeEnter: (to, from, next) => {
                const accessToken = localStorage.getItem('accessToken');
                if (!accessToken) {
                    alert('로그인이 필요합니다.')
                    next('/');

                } else {
                    next();
                }
            }
        },
        {
            path: '/product/payment/:id',
            component: PaymentPage,
                            beforeEnter: (to, from, next) => {
                const accessToken = localStorage.getItem('accessToken');
                if (!accessToken) {
                    alert('로그인이 필요합니다.')
                    next('/');

                } else {
                    next();
                }
            }
        },
        {
            path: '/product/upload/:id',
            component: UploadPage,
            name: 'uploadPage',
                            beforeEnter: (to, from, next) => {
                const accessToken = localStorage.getItem('accessToken');
                if (!accessToken) {
                    alert('로그인이 필요합니다.')
                    next('/');

                } else {
                    next();
                }
            }
        },
        {
            path: '/product/review/:id',
            component: ReviewPage,
            name: 'reviewPage',
                beforeEnter: (to, from, next) => {
                const accessToken = localStorage.getItem('accessToken');
                if (!accessToken) {
                    alert('로그인이 필요합니다.')
                    next('/');

                } else {
                    next();
                }
            }
        },
        {
            path: '/user/mypage/comment',
            component: CommentPage,
            name: 'CommentPage',
            beforeEnter: (to, from, next) => {
                const accessToken = localStorage.getItem('accessToken');
                if (!accessToken) {
                    alert('로그인이 필요합니다.')
                    next('/');
                } else {
                    next();
                }
            }
        },
        {
            path: '/',
            component: ProductlistPage,
        },
    ]
});