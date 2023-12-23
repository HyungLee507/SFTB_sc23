<template>
  <div class="buyertradingpage">
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
              <tr>
                <th>이미지</th>
                <th>상품명</th>
                <th>가격</th>
                <th>사이즈</th>
                <th></th>
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
                <td>
                  <button v-if="isCancellationAllowed(item.createdDate)" @click="openCancelConfirmation(index)"
                          class="delete-button">결제취소
                  </button>
                  <span v-else style="color: rgb(248, 42, 42);">결제 취소 불가</span>
                </td>
              </tr>
              </tbody>
            </table>
            <b-modal ref="cancelModal" title="결제 취소 확인" @ok="removeItem(selectedItemIndex)" ok-title="예"
                     cancel-title="아니오">
              <p>결제를 취소하시겠습니까?</p>
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
import axios from 'axios';

export default {
  components: {
    ButtonList,
  },
  data() {
    return {
      ordersProducts: [],
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
          .get('/orderItems')
          .then((response) => {
            this.ordersProducts = response.data.map((item) => ({
              image: item.image,
              name: item.name,
              price: item.price,
              shoeSize: item.shoeSize,
              orderId: item.orderId,
              createdDate: item.createdDate,

            }));
          })
          .catch(() => {
          });
    },
    getImageUrl(imageName) {
      return `/product/${imageName}`;
    },


    removeItem(index) {
      const orderId = this.ordersProducts[index].orderId;

      axios
          .put(`/refundItem/${orderId}`)
          .then(() => {
            this.ordersProducts.splice(index, 1);
            alert("결제가 취소되었습니다.");
          })
          .catch(() => {
            alert("결제취소 실패.");
          });
    },

    isCancellationAllowed(createdDate) {
      const oneHourAgo = new Date();
      oneHourAgo.setHours(oneHourAgo.getHours() - 1);
      return new Date(createdDate) > oneHourAgo;
    },

    openCancelConfirmation(index) {
      this.selectedItemIndex = index;
      this.$refs.cancelModal.show();
    },

    formatPrice(price) {
      return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
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
  background-color: #4CAF50;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
}

.delete-button {
  color: red;
  background-color: white;
  border: 2px solid red;
  font-size: 16px;
  cursor: pointer;
  font-weight: bold;
}

.select {
  margin-bottom: 40px;
  font-weight: bold;
}

.dealing-products {
  background-color: black;
  color: white;
  font-weight: bold;
  text-decoration: underline;
  border: 1px solid black;

}

.completed-products {
  background-color: white;
  color: grey;
  border: 1px solid grey;
  font-weight: bold;

}

.buyertradingpage {
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

