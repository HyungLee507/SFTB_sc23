<template>
  <div class="container d-flex justify-content-center">
    <b-form @submit="submitForm" v-if="show">
      <b-form-group
        id="input-group-1"
        label="Email:"
        label-for="input-1"
      >
        <b-form-input
          type="email"
          v-model="form.Email"
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
        Email: '',
        password: '',
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
