<template>
  <div>
    <img src="@/assets/logo.png" alt="Logo" class="logo">
    <b-navbar :key="navKey" toggleable="lg" type="dark" class="nav-bar">
      <b-navbar-brand to="/" style="font-size: 40px; margin-right: 40px;">VFMarket</b-navbar-brand>

      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <b-nav-item v-if="loggedIn" to="/product/registration">상품등록</b-nav-item>
        </b-navbar-nav>

        <b-navbar-nav class="ml-auto">
          <b-nav-form>
            <b-form-input size="sm" class="mr-sm-2" placeholder="Search"></b-form-input>
            <b-button size="sm" my-2 my-sm-0 type="submit">Search</b-button>
          </b-nav-form>

          <b-nav-item v-if="!loggedIn" to="/account/login">로그인</b-nav-item>
          <b-nav-item v-if="!loggedIn" to="/account/join">회원가입</b-nav-item>
          <b-nav-item v-if="loggedIn" @click="logout">로그아웃</b-nav-item>
          <b-nav-item v-if="loggedIn" to="/user/mypage">마이페이지</b-nav-item>
          <b-nav-item v-if="loggedIn" @click="toggleModal">알림</b-nav-item>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
    <b-modal v-model="showModal" title="알림" @ok="closeModal">
      <ul>
        <li v-for="notification in notifications" :key="notification.id">
          {{ notification.message }}
        </li>
      </ul>
      <b-button @click="refreshNotifications">새로고침</b-button>
    </b-modal>
  </div>
</template>

<script>
import axios from 'axios';
import { EventBus } from '@/event-bus.js';


export default {
  name: "HeaderNav",
  data() {
    return {
      navKey: 0,
      token: localStorage.getItem('accessToken'),
      notifications: [],
      showModal: false,
      loggedIn: false,
    };
  },
  created() {
    axios.interceptors.request.use((config) => {

      const token = localStorage.getItem('accessToken');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;

      }
      return config;
    }, function (error) {

      return Promise.reject(error);
    });
      if(this.token) {
        this.loggedIn = true;
      }
      EventBus.$on('loggedIn', (status) => {
      this.loggedIn = status
    });
  },
  computed: {
    isTokenPresent() {
      return this.token !== null;
    }
  },
  methods: {
    setToken(token) {
      localStorage.setItem('accessToken', token);
      this.token = token;
    },
    removeToken() {
      localStorage.removeItem('accessToken');
      this.token = null;
    },
    logout() {
      this.removeToken();
      localStorage.removeItem('refreshToken');
      this.loggedIn = false;
      EventBus.$emit('loggedIn', false);
      if (this.$router.currentRoute.path !== '/account/login') {
        this.$router.push('/account/login');
      } else {
        this.$router.push('/');
      }
      this.navKey++;
    },
    toggleModal() {
      this.getNotifications();
      this.showModal = !this.showModal;

    },
    closeModal() {
      this.showModal = false;
    },
    refreshNotifications() {
      this.getNotifications();
    },
    getNotifications() {
      axios.get('/notifications')
          .then(response => {
            this.notifications = response.data;
          })
          .catch(error => {
            console.error(error);
          });
    },
  },
  mounted() {
    this.refreshNotifications();
  }
};
</script>

<style scoped>
.nav-bar{
  background: black;
  font-family: impact, sans-serif;
}

.navbar-dark .navbar-nav .nav-link {
    color: rgba(230, 230, 230, 0.87);
    font-family: 'Noto Sans KR', sans-serif;
    font-size: large;
}

.form-inline {
    font-family: 'Noto Sans KR', sans-serif;
}

.logo {
  margin-bottom: 40px;
  width: 150px;
  height: auto;
}
</style>
