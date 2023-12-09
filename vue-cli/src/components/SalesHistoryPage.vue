<template>
  <div class="saleshistorypage">
    <b-container class="full-container">
      <b-row>
        <b-col md="12">
          <h1 class="h1">판매내역</h1>
          <button @click="goToSellingStatus('product-register')" class="status-button product-register-button">상품등록
          </button>
        </b-col>
        <div class="long-line"></div>
      </b-row>
      <b-row>
        <b-col md="3">
          <button-list/>
        </b-col>
        <b-col md="9">
          <button @click="goToSellingStatus('selling')" class="status-button">판매중인 상품</button>
          <button @click="goToSellingStatus('trading')" class="status-button">거래중인 상품</button>
          <button @click="goToSellingStatus('sold')" class="status-button">판매완료 상품</button>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
import ButtonList from './ButtonList'
import axios from "axios";
export default {
  components: {
    ButtonList
  },
  created(){
        axios.interceptors.response.use((config) => {
      return config;
    }, function (error) {
      if (error.response && error.response.status === 401) {
        alert('로그인이 필요합니다.');
        this.$router.push('/account/login');
      }
      return Promise.reject(error);
    });
  },
  methods: {
    goToSellingStatus(status) {
      if (status === 'selling') {
        this.$router.push('/user/mypage/saleshistory/selling');
      } else if (status === 'sold') {
        this.$router.push('/user/mypage/saleshistory/sold');
      } else if (status === 'product-register') {
        this.$router.push('/product/registration');
      } else if (status === 'trading') {
        this.$router.push('/user/mypage/saleshistory/trading');
      }

    }
  }
};
</script>

<style scoped>
.status-button {
  width: 150px;
  height: 40px;
  margin-right: 80px;
  border-radius: 5px;
  font-weight: bold;
}

.product-register-button {
  background-color: black;
  color: white;
  font-weight: bold;
  border: 1px solid black;
  margin-left: 700px;
  border-radius: 5px;
}

.saleshistorypage {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: bold;
  margin-bottom: 400px;
}

.full-container {
  margin: 0;
  padding: 0;
  width: 100%;
  max-width: 1200px;
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
</style>