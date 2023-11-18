<template>
  <div class="orderspage">
    <b-container>
      <br>
      <b-row>
        <b-col md="12">
          <h1>판매내역 </h1>
          <button @click="goToSellingStatus('product-register')" class="status-button product-register-button">상품등록
          </button>
          <br><br>
        </b-col>
      </b-row>
      <b-row>
        <b-col md="3">
          <button-list/>
        </b-col>
        <b-col md="9">
          <button @click="goToSellingStatus('selling')" class="status-button">판매중인 상품</button>
          <button @click="goToSellingStatus('sold')" class="status-button">판매완료 상품</button>

        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
import ButtonList from './ButtonList'

export default {
  components: {
    ButtonList
  },
  methods: {
    goToSellingStatus(status) {
      if (status === 'selling') {
        this.$router.push('/user/mypage/saleshistory/selling');
      } else if (status === 'sold') {
        this.$router.push('/user/mypage/saleshistory/sold');
      } else if (status === 'product-register') {
        this.$router.push('/product/registration');
      }
    }
  }
};
</script>

<style scoped>
.status-button {
  width: 150px;
  height: 40px;
  margin-right: 10px;
}

.product-register-button {
  background-color: #007bff;
  color: white;
  margin-left: 500px;
}
</style>