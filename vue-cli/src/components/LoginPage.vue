<template>
  <div class="container d-flex justify-content-center">
    <b-form @submit="submitForm" v-if="show">
      <b-form-group id="input-group-1" label="이메일" label-for="input-1">
        <b-form-input type="email" v-model="form.email" id="input-1" aria-describedby="email-help-block"
          required></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-2" label="비밀번호" label-for="input-2">
        <b-form-input type="password" v-model="form.password" id="input-2" aria-describedby="password-help-block"
          required></b-form-input>
      </b-form-group>

      <b-button type="submit" variant="primary">로그인</b-button>
      <b-button to="/account/join" variant="primary">회원가입</b-button>
      <b-button to="/account/findpassword" variant="primary">비밀번호 찾기</b-button>
      <button @click="loginWithNaver">
        <img src="https://static.nid.naver.com/oauth/small_g_in.PNG" alt="naver login" class="naver-logo" />
      </button>


      <!-- Error message display -->
      <div v-if="error" class="alert alert-danger">
        {{ error }}
      </div>
    </b-form>

  </div>
</template>

<script>
import axios from 'axios';
import router from '@/router';

export default {
  data() {
    return {
      show: true, // 이 부분을 확인해보세요.
      form: {
        email: '',
        password: '',
      },
      error: null,
    };
  },
  methods: {
    async submitForm() {
      try {
        const response = await axios.post('#', this.form);
        localStorage.setItem('token', response.data.token);
        axios.defaults.headers.common['Authorization'] = `Bearer ${response.data.token}`;
        router.push('/');
      } catch (error) {
        console.error(error);
      }
    },
    loginWithNaver() {
      window.location.href = '/oauth2/authorization/naver';
    }
  },

};
</script>
<style scoped>
.naver-logo {
  width: 100px;

  height: auto;


}
</style>