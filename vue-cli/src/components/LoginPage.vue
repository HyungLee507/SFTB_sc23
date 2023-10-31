<template>
  <div class="container d-flex justify-content-center">
    <b-form @submit="submitForm" v-if="show">
      <b-form-group
        id="input-group-1"
        label="User Email:"
        label-for="input-1"
      >
        <b-form-input
          type="email"
          v-model="form.userEmail"
          id="input-1"
          aria-describedby="email-help-block"
          placeholder="Enter Email"
          required
        ></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-2" label="Password:" label-for="input-2">
        <b-form-input type="password" v-model="form.password" id="input-2" aria-describedby="password-help-block" placeholder="Enter Password" required></b-form-input>
      </b-form-group>

      <b-button type="submit" variant="primary">로그인</b-button> <b-button href="/account/join" variant="primary">회원가입</b-button> 

      <!-- Error message display -->
      <div v-if="error" class="alert alert-danger">
        {{ error }}
      </div>
    </b-form>
  </div>
</template>

<script>
import axios from 'axios';


export default {
  data() {
    return {
      show: true,
      form: {
        userEmail: '',
        password: '',
      },
      error: null,
    };
  },
  methods: {
    async submitForm() {
      try {
        const response = await axios.post('/account/login', this.form);

        if (response.status == 200) {
          alert("로그인에 성공했습니다");
          //로그인 성공
          //로그인 유지를 위한 토큰 생성
          const token = response.headers['Authorization'];
          localStorage.setItem('token',token);
          
          //로그인 상태 업데이트
          this.isLoggedIn = true;
          
        }
        else{
          alert("로그인에 실패했습니다!");
        }
        
      } catch (error) {
        
        alert("로그인에 실패했습니다!");
        console.error(error);
      }
    },
  },
};
</script>
