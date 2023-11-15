<template>
  <div>
    <b-form @submit.prevent="submitForm">
      <b-form-group id="input-group-1" label="이름:" label-for="input-1">
        <b-form-input id="input-1" v-model="name" required></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-2" label="신발 사이즈:" label-for="input-2">
        <b-form-input id="input-2" v-model="shoeSize" required></b-form-input>
      </b-form-group>

      <b-button type="submit" variant="primary">제출</b-button>
    </b-form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      name: '',
      shoeSize: ''
    }
  },
  methods: {
    submitForm() {
      axios.post('/user/update', {
        //여기에서 이메일을 줄 수 가 있나?
        name: this.name,
        shoeSize: this.shoeSize
      })
          .then(response => {
            this.$router.push('/product/list');
            console.log(response);
          })
          .catch(error => {
            console.log(error);
          });
    }
  }
}
</script>
