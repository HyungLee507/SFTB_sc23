<template>
  <div class="tradingpage">
    <b-container class="full-container">
      <b-row>
        <b-col md="12">
          <h1 class="h1">거래중인 상품</h1>
          <button @click="goToSellingStatus('product-register')"
                  class="status-button product-register-button">상품등록
          </button>
        </b-col>
        <div class="long-line"></div>
      </b-row>
      <b-row>
        <b-col md="3">
          <button-list/>
        </b-col>
        <b-col md="9">
          <div v-if="tradingProducts.length > 0">
            <table class="table">
              <thead>
              <tr>
                <th style="width: 20%;">이미지</th>
                <th style="width: 20%;">상품명</th>
                <th style="width: 10%;">가격</th>
                <th style="width: 25%">구매자 주소</th>
                <th>배송처리</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(item, index) in tradingProducts" :key="index">
                <td>
                  <img :src=getImageUrl(item.image) alt="Image" width="50">
                </td>
                <td>{{ item.name }}</td>
                <td>{{ formatPrice(item.price) }}</td>
                <td>
                  {{ item.address }}
                </td>
                <td>
                  <button class="bill-button"
                          @click="showBillModal = true; selectedProductId = item.id">
                    운송장번호 입력
                  </button>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
          <div v-else>
            <p>거래중인 상품이 없습니다.</p>
          </div>
        </b-col>
      </b-row>
      <div class="shipping-note">
        <p class="guide-text">※상품을 배송한 후 운송장번호를 입력해주세요.</p>
      </div>
    </b-container>
    <b-modal v-model="showBillModal" title="운송장번호 입력" hide-footer>
      <b-form @submit.prevent="saveBillNumber">
        <b-form-group label="운송장번호" style="font-weight: bold;">
          <b-form-input v-model="billInput" required></b-form-input>
        </b-form-group>
        <b-form-group label="택배사" style="font-weight: bold;">
          <b-form-radio-group v-model="deliveryCompany" :options="deliveryCompanies"></b-form-radio-group>
        </b-form-group>
        <b-button type="submit" variant="primary">배송완료</b-button>
      </b-form>
    </b-modal>
  </div>
</template>

<script>
import ButtonList from './ButtonList';
import axios from 'axios';

export default {
  components: {
    ButtonList,
  },
  data() {
    return {
      tradingProducts: [],
      billInput: '',
      showBillModal: false,
      selectedProductId: '',
      deliveryCompany: '',
      deliveryCompanies: [
        'CJ대한통운',
        '한진택배',
        '롯데택배',
        '우체국택배',
      ],
    };
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

    this.getTradingProducts();

  },


  methods: {
    getTradingProducts() {
      axios
          .get('/tradeItems')
          .then((response) => {
            this.tradingProducts = response.data.map((item) => ({
              image: item.image,
              name: item.name,
              price: item.price,
              id: item.orderId,
              address: item.address,
            }));
          })
          .catch(() => {
          });
    },
    getImageUrl(imageName) {
      return `/product/${imageName}`;
    },

    goToProductRegisterPage() {
      const targetRoute = "/product/registration";
      this.$router.push(targetRoute).catch(() => {
      });
    },

    saveBillNumber() {
      const productId = this.selectedProductId;
      const billingNumber = this.billInput;
      const deliveryCompany = this.deliveryCompany;

      axios.put('/product-bill', {
        orderId: productId,
        billingNumber: billingNumber,
        deliveryCompany: deliveryCompany,
      })
          .then(() => {
            alert('배송완료처리 되었습니다.')
            const indexToRemove = this.tradingProducts.findIndex(item => item.id === productId);
            if (indexToRemove !== -1) {
              this.tradingProducts.splice(indexToRemove, 1);
            }
          })
          .catch(() => {
            alert('배송완료처리에 실패했습니다.')
          });


      this.showBillModal = false;
    },
    formatPrice(price) {
      return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    },

  },
};
</script>


<style scoped>
.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.table {
  border-collapse: collapse;
  width: 100%;
  table-layout: fixed;
  word-wrap: break-word;
  min-width: 800px;
}


th {
  text-align: center;
  padding: 10px;
  border-bottom: 1px solid #ddd;
  font-weight: bold;
  background-color: #000000e5;
  color: white;
}

td {
  text-align: center;
  padding: 10px;
  border-bottom: 1px solid #ddd;
  font-weight: bold;
}

img {
  width: 90px;
  height: 120px;
}

.product-register-button {
  background-color: black;
  color: white;
  font-weight: bold;
  border: 1px solid black;
  margin-left: 700px;
  border-radius: 5px;
  width: 150px;
  height: 40px;
  text-align: center;
}

.shipping-note {
  text-align: right;
  margin-top: 20px;
}

.guide-text {
  color: rgba(0, 0, 0, 0.5);
  font-size: 14px;
}

.tradingpage {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: bold;
  margin-bottom: 400px;
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

.full-container {
  margin: 0;
  padding: 0;
  width: 100%;
  max-width: 1200px;
}

.bill-button {
  background-color: white;
  color: black;
  padding: 10px 20px;
  border: 2px solid rgb(3, 160, 3);
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
}
</style>