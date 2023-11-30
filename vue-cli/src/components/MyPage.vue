<template>
  <div class="mypage">
    <b-container>
      <b-row>
        <b-col md="12">
          <h1 style="margin-bottom: 40px; margin-top: 40px;">주문내역</h1>
        </b-col>
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
                <th>주문취소</th>
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
                  <!-- <button v-if="isCancellationAllowed(item.createdDate)" @click="openCancelConfirmation(index)" class="delete-button">결제취소</button> -->
                  <!-- <span v-else><button @click="openReviewModal(index)" class="review-button">리뷰작성</button></span> -->
                  <button @click="openReviewModal(index)" class="review-button">리뷰작성</button>
                </td>
              </tr>
              </tbody>
            </table>
            <b-modal ref="cancelModal" title="결제 취소 확인" @ok="removeItem(selectedItemIndex)" ok-title="예"
                     cancel-title="아니오">
              <p>결제를 취소하시겠습니까?</p>
            </b-modal>
            <b-modal ref="reviewModal" id="reviewModal" title="상품 리뷰 작성" hide-footer>
              <ReviewPage/>
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


    // const sampleReviews = [
    //   { images: "이미지", name: "반팔", price: "10000" , shoeSize: "100", orderId: 1, createdDate: new Date(Date.now() - 0.5 * 60 * 60 * 1000) },
    //   { images: "이미지", name: "긴팔", price: "20000", shoeSize: "110", orderId: 2, createdDate: new Date(Date.now() - 5 * 60 * 60 * 1000) },
    //   { images: "이미지", name: "바지", price: "30000", shoeSize: "90", orderId: 3, createdDate: new Date(Date.now() - 5 * 60 * 60 * 1000) },
    //   { images: "이미지", name: "신발", price: "40000", shoeSize: "100", orderId: 4, createdDate: new Date(Date.now() - 5 * 60 * 60 * 1000) },
    // ];
    // //가상의 리뷰 데이터
    // this.ordersProducts = sampleReviews;


    this.getOrdersProducts();
  },

  methods: {
    getOrdersProducts() {
      axios
          .get('/orderItems')   //api 수정
          .then((response) => {
            this.ordersProducts = response.data.map((item) => ({
              image: item.image,
              name: item.name,
              price: item.price,
              shoeSize: item.shoeSize,
              //category: item.category,
              //description: item.description,
              //status: item.status,     //배송중, 배송완료?
              orderId: item.id,
              createdDate: item.createdDate,
            }));
          })
          .catch((error) => {
            console.error(error);
          });
    },
    getImageUrl(imageName) {
      return `http://localhost:8080/product/${imageName}`;
    },


    removeItem(index) {
      const orderId = this.ordersProducts[index].orderId;

      // axios
      //     .delete(`/delete-item?itemId=${orderId}`)   //api 맞게 수정
      //     .then(() => {
      //       this.ordersProducts.splice(index, 1);
      //       alert('결제가 취소되었습니다.');
      //     })
      //     .catch((error) => {
      //       console.error(error);
      //     });
      axios
          .put(`/refundItem/${orderId}`)   //api 맞게 수정
          .then(() => {
            this.ordersProducts.splice(index, 1);
            alert("결제 취소 완료!");
          })
          .catch((error) => {
            console.error(error);
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

    openReviewModal(index) {
      // this.selectedItemIndex = index;
      // const orderId = this.ordersProducts[index].orderId;
      // this.$refs.reviewModal.orderId = orderId;
      // this.$refs.reviewModal.show();
      this.selectedItemIndex = index;
      const orderId = this.ordersProducts[index].orderId;
      this.$router.push({name: 'reviewPage', params: {id: orderId}});
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
  font-weight: bold;
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
  background-color: red;
  color: white;
  border: none;
  font-size: 16px;
  cursor: pointer;
}
</style>
