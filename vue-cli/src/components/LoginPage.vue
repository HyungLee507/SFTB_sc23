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
          v-model="form.email"
          id="input-1"
          aria-describedby="email-help-block"
          required
        ></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-2" label="비밀번호" label-for="input-2">
        <b-form-input type="password" v-model="form.password" id="input-2" aria-describedby="password-help-block" required></b-form-input>
      </b-form-group>

      <b-button type="submit" variant="primary">로그인</b-button> 
      <b-button to="/account/join" variant="primary">회원가입</b-button>
      <b-button to="/account/findpassword" variant="primary">비밀번호 찾기</b-button>
      

      <!-- Error message display -->
      <div v-if="error" class="alert alert-danger">
        {{ error }}
      </div>
    </b-form>
    <button href="/oauth2/authorization/naver">img(src="C:\Users\kwjeon98\Documents\SFTB_sc23\vue-cli\src\assets\naverlogo.png)</button>
  </div>
</template>

<script>
import axios from 'axios';
import router from '@/router';


export default {
  data() {
    return {
      show: true,
      form: {
        email: '',
        password: '',
        token: '',
      },
      error: null,
    };
  },
  methods: {
    async submitForm() {
      
        await axios.post('/account/login', this.form)
        .then((res) => {
          if(res.status===200){
            console.log(res.data);
            this.token = this.res.data.data.token;
            console.log("토큰:"+this.token);
            this.$cookie.set("accesstoken",res.data.data.token,1);
            axios.defaults.headers.common["x-access-token"] = res.data.data.token;
            alert("로그인 성공");
            router.push("/");
          }
        }).catch((err)=>{
          alert("로그인 실패" + err)
        });
    },
  },
  
};
</script>
