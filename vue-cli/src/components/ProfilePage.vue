<template>
  <div class="profilepage">
    <b-container>
      <br>
      <b-row>
        <b-col md="12">
          <h1>내정보</h1>
          <br><br>
        </b-col>
      </b-row>
      <b-row>
        <b-col md="3">
          <button-list/>
        </b-col>
        <b-col md="9">
          <div>
            <h2>사용자 정보</h2>
            <p>이름: {{ user.userName }}</p>
            <p>이메일: {{ user.email }}</p>
            <p>상의 사이즈: {{ user.size }}</p>
            <button @click="editMode = true">수정</button>
          </div>
          <div v-if="editMode">
            <form @submit.prevent="submitForm">
              <label>이름:</label>
              <input type="text" v-model="user.name">
              <label>이메일:</label>
              <input type="text" v-model="user.email">
              <label>신발 사이즈:</label>
              <input type="text" v-model="user.footsize">
              <button type="submit">저장</button>
              <button @click="editMode = false">취소</button>
            </form>
          </div>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
import ButtonList from './ButtonList'
import axios from 'axios';

export default {

  components: {
    ButtonList
  },
  data() {
    return {
      user: {
        userName: '',
        email: '',
        size: '',
      },
      editMode: false
    }
  },
  created() {
    axios.interceptors.request.use((config) => {
      const token = localStorage.getItem('accessToken'); 
      if (token) {
        config.headers.Authorization = `Bearer ${token}`; 
      }
      console.log(config.headers.Authorization);
      return config;
    }, function (error) {

      return Promise.reject(error);
    });

    axios.get('/user')
        .then(response => {
          this.user = response.data;
        })
        .catch(error => {
          console.log(error);
        });

  },
  methods: {
    submitForm() {
      axios.put('/user-Info/update', this.user)
          .then(() => {
            this.editMode = false;
          })
          .catch(() => {
          });
    }
  }
};
</script>

<style scoped></style>