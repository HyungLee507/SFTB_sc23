<template>
  <div class="sellingpage">
    <b-container>
      <b-row>
        <b-col md="12">
          <h1 style="margin-top: 40px;">판매중인 상품</h1>
          <button @click="goToProductRegisterPage" class=" product-register-button">상품등록</button>
        </b-col>
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
                  <!-- <img :src="item.images[0]" alt="Image" width="50"> -->
                </td>
                <td>{{ item.name }}</td>
                <td>{{ item.price }}</td>
                <td>
                  <button @click="goToSellingProductInformationPage(item.id)">수정</button>
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

    this.getSellingProducts();
  },

  methods: {
    getSellingProducts() {
      axios
          .get('/salesItems')   //api 수정
          .then((response) => {
            this.sellingProducts = response.data.map((item) => ({
              image: item.imageName,
              name: item.itemName,
              price: item.price,
              //shoeSize: item.shoeSize,
              //category: item.category,
              //description: item.description,
              id: item.itemId,
            }));
          })
          .catch((error) => {
            console.error(error);
          });
    },
    getImageUrl(imageName) {
      return `http://localhost:8080/product/${imageName}`;
      // return `https://c6d8-14-63-41-207.ngrok-free.app/product/${imageName}`;
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

th,
td {
  text-align: left;
  padding: 10px;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #f2f2f2;
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

.product-register-button {
  background-color: #007bff;
  color: white;
  margin-left: 500px;
  margin-bottom: 30px;
  width: 150px;
  height: 40px;
}

</style>