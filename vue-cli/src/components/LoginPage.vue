<template>
  <div class="container d-flex justify-content-center">
    <b-form @submit="submitForm" v-if="show">
      <b-form-group
        id="input-group-1"
        label="이메일"
        label-for="input-1"
      >
        <b-form-input
          type="email"
          v-model="form.Email"
          id="input-1"
          aria-describedby="email-help-block"
          required
        ></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-2" label="비밀번호" label-for="input-2">
        <b-form-input type="password" v-model="form.Password" id="input-2" aria-describedby="Password-help-block" required></b-form-input>
      </b-form-group>

      <b-button type="submit" variant="primary">로그인</b-button> <b-button to="/account/join" variant="primary">회원가입</b-button> 

      <!-- Error message display -->
      <div v-if="error" class="alert alert-danger">
        {{ error }}
      </div>
    </b-form>
  </div>
</template>

<script>
import axios from 'axios';//태스트


export default {
  data() {
    return {
      show: true,
      form: {
        Email: '',
        Password: '',
      },
      error: null,
    };
  },
  methods: {
    async submitForm() {
      
        await axios.post('/account/login', this.form)
        .then(() => {
          alert("로그인 성공")
          this.$router.push('/account/login');
        }).catch(()=>{
          alert("로그인 실패")
        });
    },
  },
};
</script>
