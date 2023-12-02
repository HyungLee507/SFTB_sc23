<template>
  <div class="container d-flex justify-content-center">
    <b-form @submit.prevent="submitForm" v-if="show">
      <b-form-group id="input-group-1" label="이메일" label-for="input-1">
        <b-form-input type="email" v-model="form.email" id="input-1" aria-describedby="email-help-block"
          required></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-2" label="비밀번호" label-for="input-2">
        <b-form-input type="password" v-model="form.password" id="input-2" aria-describedby="password-help-block"
          required></b-form-input>
      </b-form-group>

      <b-button @click="submitForm" variant="primary">로그인</b-button>
      <b-button to="/account/join" variant="primary">회원가입</b-button>
      <b-button to="/account/findpassword" variant="primary">비밀번호 찾기</b-button>



      <div v-if="error" class="alert alert-danger">
        {{ error }}
      </div>
    </b-form>

  </div>
</template>

<script>
import axios from 'axios';
import { EventBus } from '@/event-bus.js';


export default {
  data() {
    return {
      show: true, 
      form: {
        email: '',
        password: '',
      },
      error: null,
    };
  },
  created() {
    axios.interceptors.request.use(function (config) {
      return config;
    }, function (error) {
      return Promise.reject(error);
    });
    axios.interceptors.response.use(function (response) {
      return response;
    }, function (error) {
      if (error.response.status === 401) {
        alert('재로그인이 필요합니다.');
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        this.$router.push('/account/login');
      }
      return Promise.reject(error);
    });
  },
  methods: {

    async submitForm($event) {
      $event.preventDefault();
      await axios.post('/login', this.form)
        .then(response => {
          EventBus.$emit('loggedIn', true);
          const accessToken = response.headers['authorization'];
          const refreshToken = response.headers['authorization-refresh'];
          if (refreshToken) { 
            localStorage.setItem('refreshToken', refreshToken);
            axios.defaults.headers.common['Refresh'] = refreshToken;
          }
          localStorage.setItem('accessToken', accessToken);
          axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
          this.$router.push('/product/list');
        })
        .catch(error => {
          console.log(error);
          alert('이메일 또는 비밀번호가 일치하지 않습니다.');
        });
    },
  },

};
</script>
<style scoped>
.naver-logo {
  width: 100px;

  height: auto;


}
</style>