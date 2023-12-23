<template>
  <div class="sellingpage">
    <b-container class="full-container">
      <b-row>
        <b-col md="12">
          <h1 class="h1">판매중인 상품</h1>
          <button @click="goToProductRegisterPage" class=" product-register-button">상품등록</button>
        </b-col>
        <div class="long-line"></div>
      </b-row>
      <b-row>
        <b-col md="3">
          <button-list/>
        </b-col>
        <b-col md="9">
          <div v-if="sellingProducts.length > 0">
            <table>
              <thead>
              <tr>
                <th>이미지</th>
                <th>상품명</th>
                <th>가격</th>
                <th></th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(item, index) in sellingProducts" :key="index">
                <td>
                  <img :src=getImageUrl(item.image) alt="Image" width="50">
                </td>
                <td>{{ item.name }}</td>
                <td>{{ formatPrice(item.price) }}</td>
                <td>
                  <button class="edit-button" @click="goToSellingProductInformationPage(item.id)">수정</button>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
          <div v-else>
            <p>판매 중인 상품이 없습니다.</p>
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
      sellingProducts: [],
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

    this.getSellingProducts();
  },

  methods: {
    getSellingProducts() {
      axios
          .get('/salesItems')
          .then((response) => {
            this.sellingProducts = response.data.map((item) => ({
              image: item.imageName,
              name: item.itemName,
              price: item.price,
              id: item.itemId,
            }));
          })
          .catch(() => {
          });
    },
    getImageUrl(imageName) {
      return `/product/${imageName}`;
      // return `https://vfmarket.store/product/${imageName}`;

    },


    goToProductRegisterPage() {
      const targetRoute = "/product/registration";
      this.$router.push(targetRoute).catch(() => {
      });
    },
    goToSellingProductInformationPage(productId) {
      const targetRoute = `/user/mypage/saleshistory/sellingproduct/${productId}`;
      this.$router.push(targetRoute).catch(() => {
      });
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

.sellingpage {
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

.edit-button {
  background-color: white;
  color: black;
  padding: 10px 20px;
  border: 2px solid black;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
}
</style>