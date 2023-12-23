<template>
  <div class="cartpage">
    <b-container class="full-container">
      <b-row>
        <b-col md="12">
          <h1 class="h1">장바구니</h1>
        </b-col>
        <div class="long-line"></div>
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
                <th>이미지</th>
                <th>상품명</th>
                <th>가격</th>
                <th>장바구니 삭제</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(item, index) in cartItems" :key="index">
                <td>
                  <img :src=getImageUrl(item.image) alt="Image" width="50">
                </td>
                <td>
                  <router-link :to="'/product-list/' + item.id">
                    {{ item.name }}
                  </router-link>
                </td>
                <td>{{ formatPrice(item.price) }}</td>
                <td>
                  <button @click="confirmRemoveItem(index)" class="delete-button">X</button>
                </td>
              </tr>
              </tbody>
            </table>
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
      axios.interceptors.response.use((config) => {
      return config;
    }, function (error) {
      if (error.response && error.response.status === 401) {
        alert('로그인이 필요합니다.');
        this.$router.push('/account/login');
      }
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
          .catch(() => {
          });
    },
    getImageUrl(imageName) {
      // return `https://vfmarket.store/product/${imageName}`;
      return `/product/${imageName}`;

    },
    removeItem(index) {
      const itemId = this.cartItems[index].id;
      axios
          .delete(`/delete-item?itemId=${itemId}`)
          .then(() => {
            this.cartItems.splice(index, 1);
          })
          .catch(() => {
          });
    },
    formatPrice(price) {
      return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    },
    confirmRemoveItem(index) {
      const confirmed = window.confirm("상품을 삭제하시겠습니까?");
      if (confirmed) {
        this.removeItem(index);
      }
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
  background-color: transparent;
  color: #000000;
  border: none;
  font-size: 16px;
  cursor: pointer;
  font-weight: bold;
}

.cartpage {
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