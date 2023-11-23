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
                <!-- <th>배송상태</th> -->
                <th>주문취소</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(item, index) in ordersProducts" :key="index">
                <td>
                  <img :src=getImageUrl(item.image) alt="Image" width="50">
                  <!-- <img :src="item.images[0]" alt="Image" width="50"> -->
                </td>
                <td>{{ item.name }}</td>
                <td>{{ item.price }}</td>
                <!-- <td>{{ item.status }}</td> -->
                <td>
                  <button v-if="isCancellationAllowed(item.createdDate)" @click="removeItem(index)"
                          class="delete-button">결제취소
                  </button>
                  <span v-else>취소불가</span>
                </td>
              </tr>
              </tbody>
            </table>
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
      // return `https://c6d8-14-63-41-207.ngrok-free.app/product/${imageName}`;
    },


    removeItem(index) {
      const orderId = this.ordersProducts[index].orderId;

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
      oneHourAgo.setHours(oneHourAgo.getHours() - 4);
      return new Date(createdDate) > oneHourAgo;
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

.delete-button {
  background-color: transparent;
  color: #000000;
  border: none;
  font-size: 16px;
  cursor: pointer;
}
</style>

<!--<template>-->
<!--  <div class="mypage">-->
<!--    <b-container>-->
<!--      <b-row>-->
<!--        <b-col md="12">-->
<!--          <h1 style="margin-bottom: 40px; margin-top: 40px;">주문내역</h1>-->
<!--        </b-col>-->
<!--      </b-row>-->
<!--      <b-row>-->
<!--        <b-col md="3">-->
<!--          <button-list/>-->
<!--        </b-col>-->
<!--        <b-col md="9">-->
<!--          <div v-if="ordersProducts.length > 0">-->
<!--            <table>-->
<!--              <thead>-->
<!--              <tr>-->
<!--                <th>이미지</th>-->
<!--                <th>상품명</th>-->
<!--                <th>가격</th>-->
<!--                <th>배송상태</th>-->
<!--                <th>주문취소</th>-->
<!--              </tr>-->
<!--              </thead>-->
<!--              <tbody>-->
<!--              <tr v-for="(item, index) in ordersProducts" :key="index">-->
<!--                <td>-->
<!--                  <img :src="item.images[0]" alt="Image" width="50">-->
<!--                </td>-->
<!--                <td>{{ item.name }}</td>-->
<!--                <td>{{ item.price }}</td>-->
<!--                <td>{{ item.status }}</td>-->
<!--                <td>-->
<!--                  <button v-if="item.status === '배송중'" @click="removeItem(index)"-->
<!--                          class="delete-button">X-->
<!--                  </button>-->
<!--                  <span v-else>취소불가</span>-->
<!--                </td>-->
<!--              </tr>-->
<!--              </tbody>-->
<!--            </table>-->
<!--          </div>-->
<!--          <div v-else>-->
<!--            <p>구매한 상품이 없습니다.</p>-->
<!--          </div>-->
<!--        </b-col>-->
<!--      </b-row>-->
<!--    </b-container>-->
<!--  </div>-->
<!--</template>-->

<!--&lt;!&ndash; <script>-->
<!--import ButtonList from './ButtonList';-->
<!--import axios from 'axios';-->

<!--export default {-->
<!--    components: {-->
<!--        ButtonList,-->
<!--    },-->
<!--    data() {-->
<!--        return {-->
<!--            ordersProducts: [],-->
<!--        };-->
<!--    },-->

<!--    created() {-->
<!--        this.getOrdersProducts();-->
<!--    },-->

<!--    methods: {-->
<!--        getOrdersProducts() {-->
<!--            axios-->
<!--                .get('/order')-->
<!--                .then((response) => {-->
<!--                    this.ordersProducts = response.data.map((item) => ({-->
<!--                        images: item.images,-->
<!--                        name: item.name,-->
<!--                        price: item.price,-->
<!--                        shoeSize: item.shoeSize-->
<!--                        category: item.category-->
<!--                        description: item.description,-->
<!--                        status: item.status,-->
<!--                    }));-->
<!--                })-->
<!--                .catch((error) => {-->
<!--                    console.error(error);-->
<!--                });-->
<!--        },-->
<!--        removeItem(index) {-->
<!--            this.ordersProducts.splice(index, 1);-->
<!--            // 서버에 저장된 목록 제거 로직 구현해야 함 ( 배송 취소 ? )-->
<!--        },-->
<!--    },-->
<!--};-->
<!--</script> &ndash;&gt;-->

<!--<script>-->
<!--import ButtonList from './ButtonList';-->

<!--export default {-->
<!--  components: {-->
<!--    ButtonList,-->
<!--  },-->
<!--  data() {-->
<!--    return {-->
<!--      ordersProducts: [-->
<!--        {-->
<!--          images: ["https://shopping-phinf.pstatic.net/main_1164266/11642661041.20191216104903.jpg?type=f300",],-->
<!--          name: '나이키운동화',-->
<!--          price: 10000,-->
<!--          status: '배송중',-->
<!--        },-->
<!--        {-->
<!--          images: ["https://shopping-phinf.pstatic.net/main_2955567/29555674431.20221013162919.jpg?type=f300"],-->
<!--          name: '아디다스슬리퍼',-->
<!--          price: 15000,-->
<!--          status: '배송완료'-->
<!--        },-->
<!--      ],-->
<!--    };-->
<!--  },-->
<!--  methods: {-->
<!--    removeItem(index) {-->
<!--      this.ordersProducts.splice(index, 1);-->
<!--      // 서버에 저장된 목록 제거 로직 구현해야 함  ( 배송 취소 ? )-->
<!--    },-->
<!--  },-->
<!--};-->
<!--</script>-->


<!--<style scoped>-->
<!--.container {-->
<!--  max-width: 800px;-->
<!--  margin: 0 auto;-->
<!--  padding: 20px;-->
<!--}-->

<!--table {-->
<!--  border-collapse: collapse;-->
<!--  width: 100%;-->
<!--}-->

<!--th,-->
<!--td {-->
<!--  text-align: left;-->
<!--  padding: 10px;-->
<!--  border-bottom: 1px solid #ddd;-->
<!--}-->

<!--th {-->
<!--  background-color: #f2f2f2;-->
<!--}-->

<!--img {-->
<!--  width: 50px;-->
<!--  height: 50px;-->
<!--  border-radius: 50%;-->
<!--}-->

<!--button {-->
<!--  background-color: #4CAF50;-->
<!--  color: white;-->
<!--  padding: 10px 20px;-->
<!--  border: none;-->
<!--  border-radius: 5px;-->
<!--  cursor: pointer;-->
<!--  font-size: 16px;-->
<!--}-->

<!--.delete-button {-->
<!--  background-color: transparent;-->
<!--  color: #000000;-->
<!--  border: none;-->
<!--  font-size: 16px;-->
<!--  cursor: pointer;-->
<!--}-->
<!--</style>-->