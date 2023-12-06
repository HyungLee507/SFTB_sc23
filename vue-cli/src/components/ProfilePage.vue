<template>
  <div class="profilepage">
    <b-container>
      <b-row>
        <b-col md="12">
          <h1 class="h1">내정보</h1>
        </b-col>
        <div class="long-line"></div>
      </b-row>
      <b-row>
        <b-col md="3">
          <button-list />
        </b-col>
        <b-col md="9">
          <div class="content-container">
            <form @submit.prevent="submitForm">
              <div style="margin-bottom: 30px;">
                <label for="userName" style="margin-right:80px;">이름</label>
                <input type="text" id="userName" v-model="user.userName" class="input" />
              </div>
              <div style="margin-bottom: 30px;">
                <label for="email" style="margin-right:62px;">이메일</label>
                <input type="text" id="email" v-model="user.email" class="input" />
              </div>
              <div style="margin-bottom: 30px;">
                <label for="size" style="margin-right:20px;">상의 사이즈</label>
                <input type="text" id="size" v-model="user.size" class="input" />
              </div>
            </form>
          </div>
          <button class="buttons" type="submit">저장</button>
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
      return config;
    }, function (error) {

      return Promise.reject(error);
    });
        axios.interceptors.response.use((config) => {
      return config;
    }, function (error) {
      if (error.response && error.response.status === 401) {
        alert('로그인이 필요합니다.');
        this.$router.push('/account/login');
      }
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

  }
};
</script>

<style scoped>
.profilepage {
  margin-bottom: 400px;
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: bold;
}

.h1 {
  margin-bottom: 40px;
  margin-top: 40px;
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: bold;
}

.long-line {
  height: 3px;
  background-color: black;
  margin-top: 20px;
  margin-bottom: 40px;
  margin-left: 5%;
  width: 90%;
}

.h2 {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: bold;
  margin-bottom: 40px;
}

.buttons {
  background-color: black;
  color: white;
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: bold;
  margin-top: 30px;
  margin-bottom: 30px;
  font-size: 20px;
}

.content-container {
  font-size: 20px;
  text-align: left;
}

.input {
  text-align: center;
}
</style>