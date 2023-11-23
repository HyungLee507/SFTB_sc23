<template>
  <div class="cartpage">
    <b-container>
      <br>
      <b-row>
        <b-col md="12">
          <h1>장바구니</h1>
          <br><br>
        </b-col>
      </b-row>
      <b-row>
        <b-col md="3">
          <button-list/>
        </b-col>
        <b-col md="9">
          <div v-if="cartItems.length > 0">
            <table>
              <thead>
              <tr>
                <th>선택</th>
                <th>이미지</th>
                <th>상품명</th>
                <th>가격</th>
                <th>삭제</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(item, index) in cartItems" :key="index">
                <td>
                  <input type="checkbox" v-model="item.selected"/>
                </td>
                <td>
                  <img :src=getImageUrl(item.image) alt="Image" width="50">
                </td>
                <td>{{ item.name }}</td>
                <td>{{ item.price }}</td>
                <td>
                  <button @click="removeItem(index)" class="delete-button">X</button>
                </td>
              </tr>
              </tbody>
            </table>
            <p>총 결제금액: {{ totalAmount }} 원</p>
            <button @click="onCheckout">주문하기</button>
          </div>
          <div v-else>
            <p>장바구니가 비어 있습니다.</p>
          </div>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>
<script>
import axios from 'axios';
import ButtonList from './ButtonList'

export default {
  components: {
    ButtonList
  },
  data() {
    return {
      cartItems: [],
      tableFields: [
        {key: 'selected', label: '선택'},
        {key: 'images', label: '이미지'},
        {key: 'name', label: '상품명'},
        {key: 'price', label: '가격'}
      ]
    };
  },
  computed: {
    totalAmount: function () {
      return this.cartItems.reduce(function (total, item) {
        return item.selected ? total + item.price : total;
      }, 0);
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
    this.getCartItems();
  },
  methods: {
    getCartItems() {
      axios
          .get('/cart-items')
          .then((response) => {
            this.cartItems = response.data.map(item => ({
              image: item.image,
              name: item.name,
              price: item.price,
              shoeSize: item.shoeSize,
              category: item.category,
              description: item.description,
              id: item.id,
            }));
          })
          .catch((error) => {
            console.error(error);
          });
    },
    onCheckout() {
      // 결제 로직 추가
    },
    getImageUrl(imageName) {
      return `http://localhost:8080/product/${imageName}`;
      // return `https://c6d8-14-63-41-207.ngrok-free.app/product/${imageName}`;
    },
    removeItem(index) {
      const itemId = this.cartItems[index].id;
      axios
          .delete(`/delete-item?itemId=${itemId}`)
          .then(() => {
            this.cartItems.splice(index, 1);
          })
          .catch((error) => {
            console.error(error);
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
  background-color: #F2F2F2;
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

.delete-button {
  background-color: transparent;
  color: #000000;
  border: none;
  font-size: 16px;
  cursor: pointer;
}
</style>