<template>
  <div>
    <b-navbar :key="navKey" toggleable="lg" type="dark" variant="info">
      <b-navbar-brand to="/">VFMarket</b-navbar-brand>

      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <b-nav-item v-if="isTokenPresent" to="/product/registration">상품등록</b-nav-item>
          <b-nav-item to="/product/list">상품리스트</b-nav-item>
        </b-navbar-nav>

        <b-navbar-nav class="ml-auto">
          <b-nav-form>
            <b-form-input size="sm" class="mr-sm-2" placeholder="Search"></b-form-input>
            <b-button size="sm" my-2 my-sm-0 type="submit">Search</b-button>
          </b-nav-form>

          <b-nav-item v-if="!isTokenPresent" to="/account/login">로그인</b-nav-item>
          <b-nav-item v-if="!isTokenPresent" to="/account/join">회원가입</b-nav-item>
          <b-nav-item v-if="isTokenPresent" @click="logout">로그아웃</b-nav-item>
          <b-nav-item v-if="isTokenPresent" to="/user/mypage">마이페이지</b-nav-item>
          <b-nav-item v-if="isTokenPresent" @click="toggleModal">알림</b-nav-item>
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

export default {
  name: "HeaderNav",
  data() {
    return {
      navKey: 0, // 컴포넌트를 강제로 업데이트하기 위한 키
      token: localStorage.getItem('accessToken'), // 토큰을 데이터로 관리합니다.
      notifications: [], // 알림 메시지 리스트
      showModal: false, // 모달 창 표시 여부
    };
  },
  created() {
    axios.interceptors.request.use((config) => {
      // 요청을 보내기 전에 수행할 작업
      const token = localStorage.getItem('accessToken'); // 로컬 스토리지에서 토큰을 가져옵니다.
      if (token) {
        config.headers.Authorization = `Bearer ${token}`; // 토큰이 있으면 헤더에 추가합니다.
      }
      console.log(config.headers.Authorization);
      return config;
    }, function (error) {
      // 요청 에러 처리
      return Promise.reject(error);
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
