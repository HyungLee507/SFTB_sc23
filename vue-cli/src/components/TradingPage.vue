<template>
  <div class="tradingpage">
    <b-container style="max-width: 1200px;">
      <b-row>
        <b-col md="12">
          <h1 style="margin-top: 40px;">거래중인 상품</h1>
          <button @click="goToSellingStatus('product-register')"
                  class="status-button product-register-button">상품등록
          </button>
        </b-col>
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
                <th></th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(item, index) in tradingProducts" :key="index">
                <td>
                  <img :src=getImageUrl(item.image) alt="Image" width="50">
                </td>
                <td>{{ item.name }}</td>
                <td>{{ item.price }}</td>
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
          .catch((error) => {
            console.error(error);
          });
    },
    getImageUrl(imageName) {
      return `http://localhost:8080/product/${imageName}`;
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
          .then(response => {
            console.log(response.data);
            alert('배송완료처리 되었습니다.')
            const indexToRemove = this.tradingProducts.findIndex(item => item.id === productId);
            if (indexToRemove !== -1) {
              this.tradingProducts.splice(indexToRemove, 1);
            }
          })
          .catch(error => {
            console.error(error);
            alert('배송완료처리에 실패했습니다.')
          });


      this.showBillModal = false;
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


td {
  padding: 10px;
  border-bottom: 1px solid #ddd;
  vertical-align: middle;
}

th {
  background-color: #f2f2f2;
  text-align: center;
}

img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
}

button {
  background-color: #4CAF50;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
}

.product-register-button {
  background-color: #007bff;
  color: white;
  margin-left: 500px;
  margin-bottom: 30px;
  width: 150px;
  height: 40px;
}

.shipping-note {
  text-align: right;
  margin-top: 20px;
}

.guide-text {
  color: rgba(0, 0, 0, 0.5);
  font-size: 14px;
}
</style>