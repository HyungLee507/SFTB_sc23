<template>
  <div class="soldpage">
    <b-container>
      <b-row>
        <b-col md="12">
          <h1 style="margin-top: 40px;">판매완료 상품</h1>
          <button @click="goToProductRegisterPage" class=" product-register-button">상품등록</button>
        </b-col>
      </b-row>
      <b-row>
        <b-col md="3">
          <button-list/>
        </b-col>
        <b-col md="9">
          <div v-if="soldProducts.length > 0">
            <table>
              <thead>
              <tr>
                <th>이미지</th>
                <th>상품명</th>
                <th>가격</th>
                <th>내역 삭제</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(item, index) in soldProducts" :key="index">
                <td>
                  <!-- <img :src="item.images[0]" alt="Image" width="50"> -->
                  <img :src=getImageUrl(item.image) alt="Image" width="50">
                </td>
                <td>{{ item.name }}</td>
                <td>{{ item.price }}</td>
                <td>
                  <button @click="openCancelConfirmation(index)" class="delete-button">내역 삭제</button>
                </td>
              </tr>
              </tbody>
            </table>
            <b-modal ref="cancelModal" title="내역 삭제" @ok="removeItem(selectedItemIndex)" ok-title="예"
                     cancel-title="아니오">
              <p>판매내역을 삭제하시겠습니까?</p>
            </b-modal>
            <p>총 판매금액: {{ totalAmount }} 원</p>
          </div>
          <div v-else>
            <p>판매한 상품이 없습니다.</p>
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
      soldProducts: [],
    };
  },

  computed: {
    totalAmount() {
      return this.soldProducts.reduce((total, item) => total + item.price, 0);
    },
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

    this.getSoldProducts();
  },

  methods: {
    getSoldProducts() {
      axios
          .get('/soldItems')  //판매완료 페이지
          .then((response) => {
            this.soldProducts = response.data.map((item) => ({
              image: item.image,
              name: item.name,
              price: item.price,
              shoeSize: item.shoeSize,
              // category: item.category,
              // description: item.description,
              id: item.id,
              createdDate: item.createdDate,
            }));
          })
          .catch((error) => {
            console.error(error);
          });
    },
    removeItem(index) {
      const id = this.soldProducts[index].id;

      axios
          .delete(`/delete-item?itemId=${id}`)   //api 맞게 수정
          .then(() => {
            this.soldProducts.splice(index, 1);
            alert('결제가 취소되었습니다.');
          })
          .catch((error) => {
            console.error(error);
          });
    },
    goToProductRegisterPage() {
      const targetRoute = "/product/registration";
      this.$router.push(targetRoute).catch(() => {
      });
    },
    getImageUrl(imageName) {
      return `http://localhost:8080/product/${imageName}`;
      // return `https://c6d8-14-63-41-207.ngrok-free.app/product/${imageName}`;
    },
    openCancelConfirmation(index) {
      this.selectedItemIndex = index;
      this.$refs.cancelModal.show();
    },
  },
};
</script>

<style scoped>
.product-register-button {
  background-color: #007bff;
  color: white;
  margin-left: 500px;
  margin-bottom: 30px;
  width: 150px;
  height: 40px;
}

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

.delete-button {
  background-color: transparent;
  color: red;
  border: none;
  font-size: 16px;
  cursor: pointer;
}
</style>