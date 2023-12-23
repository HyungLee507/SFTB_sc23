<template>
  <div class="mypage">
    <b-container class="full-container">
      <b-row>
        <b-col md="12">
          <h1 class="h1">주문내역</h1>
          <b-row class="select">
            <b-col md="6">
              <button class="dealing-products" @click="goToBuyerTradingPage">거래중인 상품</button>
            </b-col>
            <b-col md="6">
              <button class="completed-products" @click="goToMyPage">구매완료 상품</button>
            </b-col>
          </b-row>
        </b-col>
        <div class="long-line"></div>
      </b-row>
      <b-row>
        <b-col md="3">
          <button-list/>
        </b-col>
        <b-col md="9">
          <div v-if="ordersProducts.length > 0">
            <table>
              <thead>
              <tr class="table-head">
                <th style="width: 10%;">이미지</th>
                <th style="width: 10%;">상품명</th>
                <th style="width: 10%;">가격</th>
                <th style="width: 10%;">사이즈</th>
                <th style="width: 20%; text-align: center;">리뷰관리</th>
                <th>배송정보</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(item, index) in ordersProducts" :key="index">
                <td>
                  <img :src=getImageUrl(item.image) alt="Image" width="50">
                </td>
                <td>{{ item.name }}</td>
                <td>{{ formatPrice(item.price) }}</td>
                <td>{{ item.shoeSize }}</td>
                <td style="text-align: center;">
                  <div v-if="item.reviewSubmitted">
                    <p>리뷰작성완료</p>
                  </div>
                  <div v-else>
                    <button @click="openReviewModal(index)" class="review-button">리뷰작성</button>
                  </div>
                </td>
                <td style="text-align: center;">
                  <div>{{ item.deliveryCompany }}</div>
                  <div>운송장번호: {{ item.billingNumber }}</div>
                </td>
              </tr>
              </tbody>
            </table>
            <b-modal ref="reviewModal" id="reviewModal" title="상품 리뷰 작성" hide-footer>
              <ReviewPage @reviewSubmitted="handleReviewSubmitted"/>
            </b-modal>
          </div>
          <div v-else>
            <p>구매한 상품이 없습니다.</p>
          </div>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
import ButtonList from './ButtonList';
import ReviewPage from './ReviewPage';
import axios from 'axios';

export default {
  components: {
    ButtonList,
    ReviewPage,
  },
  data() {
    return {
      ordersProducts: [],
      showReviewButton: true,
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

    this.getOrdersProducts();
  },

  methods: {
    getOrdersProducts() {
      axios
          .get('/ordersDone')
          .then((response) => {
            this.ordersProducts = response.data.map((item) => ({
              image: item.image,
              name: item.name,
              price: item.price,
              shoeSize: item.shoeSize,
              orderId: item.id,
              createdDate: item.createdDate,
              reviewSubmitted: item.reviewSubmitted,
              billingNumber: item.billingNumber,
              deliveryCompany: item.deliveryCompany
            }));
          })
          .catch(() => {
          });
    },
    getImageUrl(imageName) {
      return `/product/${imageName}`;
    },


    formatPrice(price) {
      return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    },

    openReviewModal(index) {
      this.reviewSaved = false;
      this.selectedItemIndex = index;
      const orderId = this.ordersProducts[index].orderId;
      this.$router.push({name: 'reviewPage', params: {id: orderId}});
    },

    goToMyPage() {
      const targetRoute = "/user/mypage";
      this.$router.push(targetRoute).catch(() => {
      });
    },
    goToBuyerTradingPage() {
      const targetRoute = "/user/mypage/buyertrading";
      this.$router.push(targetRoute).catch(() => {
      });
    },

    handleReviewSubmitted(reviewSubmitted) {
      this.$set(this.ordersProducts, this.selectedItemIndex, {
        ...this.ordersProducts[this.selectedItemIndex],
        reviewSubmitted
      });
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

table {
  border-collapse: collapse;
  width: 100%;
  overflow-x: auto;
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

button {
  background-color: white;
  color: black;
  padding: 10px 20px;
  border: 2px solid black;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
}


.select {
  margin-bottom: 40px;
  font-weight: bold;
}

.dealing-products {
  background-color: white;
  color: gray;
  border: 1px solid gray;
}

.completed-products {
  background-color: black;
  color: white;
  font-weight: bold;
  text-decoration: underline;
  border: 1px solid black;

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

.mypage {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: bold;
  margin-bottom: 400px;
}
</style>
