import Vue from 'vue'
import Router from 'vue-router';
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'
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


new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
