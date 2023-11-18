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
            <p>이름: {{ user.name }}</p>
            <p>이메일: {{ user.email }}</p>
            <p>신발 사이즈: {{ user.footsize }}</p>
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
        name: '',
        email: '',
        footsize: '',
      },
      editMode: false
    }
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

    axios.get('/api/user')
        .then(response => {
          this.user = response.data;
        })
        .catch(error => {
          console.log(error);
        });

  },
  methods: {
    submitForm() {
      axios.put('/api/user', this.user)
          .then(response => {
            console.log(response);
            this.editMode = false;
          })
          .catch(error => {
            console.log(error);
          });
    }
  }
};
</script>

<style scoped></style>