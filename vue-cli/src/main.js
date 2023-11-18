import Vue from 'vue'
import Router from 'vue-router';
import {BootstrapVue, IconsPlugin} from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import App from './App.vue'
import router from './router'
import VueCookie from 'vue-cookie'


Vue.use(BootstrapVue)
Vue.use(IconsPlugin)
Vue.use(Router)
Vue.use(VueCookie)
Vue.config.productionTip = false

// axios.defaults.baseURL = 'http://localhost:8080'; // Spring Boot 서버의 주소로 설정
// axios.defaults.withCredentials = true; // 인증 정보를 서버로 보내도록 설정


new Vue({
    router,
    render: h => h(App),
}).$mount('#app')
