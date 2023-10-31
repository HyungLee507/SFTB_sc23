<template>
  <div class="container d-flex justify-content-center">
    <b-form @submit="submitForm" @reset="onReset" v-if="show">
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

      <b-button type="submit" variant="primary">로그인</b-button>

      <!-- Error message display -->
      <div v-if="error" class="alert alert-danger">
        {{ error }}
      </div>
    </b-form>
  </div>
</template>

<script>
import axios from 'axios';
import router from 'vue-router'; 

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

        if (response.status === 200) {
          alert("로그인에 성공했습니다");
          router.push('/');
        } 
      } catch (error) {
        // Handle the error and show an alert to the user
        alert("로그인에 실패했습니다");
        console.error(error);
      }
    },
  },
};
</script>
