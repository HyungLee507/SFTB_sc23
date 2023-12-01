<template>
  <div class="virtualfittingpage">
    <b-container>
      <br>
      <b-row>
        <b-col md="12">
          <h1>가상 피팅</h1>
          <br><br>
        </b-col>
      </b-row>
      <b-row>
        <b-col md="3">
          <button-list />
        </b-col>
        <b-col md="9">
          <div>
            <b-card-group deck>
              <div v-for="styleShot in styleShots" :key="styleShot.styleShotId">
                <b-card :title="styleShot.itemName" :img-src="getImageUrl(styleShot.styleShotName)" img-alt="Image"
                        img-top class="fixed-card-size" @click="redirectToProductList(styleShot.itemId)">
                </b-card>
                <b-button variant="danger" @click="deleteProduct(styleShot.styleShotId)">삭제</b-button>
              </div>
            </b-card-group>
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
      styleShots: [], // 상품정보를 저장할 배열
    };
  },
  created() {
    this.item_id = this.$route.params.id;
    axios.interceptors.request.use((config) => {
      // 요청을 보내기 전에 수행할 작업
      const token = localStorage.getItem('accessToken'); // 로컬 스토리지에서 토큰을 가져옵니다.
      if (token) {
        config.headers.Authorization = `Bearer ${token}`; // 토큰이 있으면 헤더에 추가합니다.
      }
      console.log(config.headers.Authorization);
      return config;
    }, function (error) {
      // 요청 에러 처리
      return Promise.reject(error);
    });
    this.fetchProducts();
  },
  methods: {
    fetchProducts() {
      axios
          .get('/vf/viewstyle')
          .then((response) => {
            this.styleShots = response.data;
            console.log(response.data);
          })
          .catch((error) => {
            console.log(error);
          });
    },
    getImageUrl(imageName) {
      return `http://localhost:8080/vf/${imageName}`;
    },
    deleteProduct(styleShotId) {
      axios
          .delete(`/vf/deletestyle?styleShotId=${styleShotId}`)
          .then((response) => {
            console.log(response.data);
            this.fetchProducts();
          })
          .catch((error) => {
            console.log(error);
          });
    },
    redirectToProductList(itemId) {
      this.$router.push(`/product-list/${itemId}`);
    },
  },
};
</script>

<style>
.b-card {
  max-width: 20rem;
  margin-bottom: 1rem;
}

.fixed-card-size {
  width: 20rem;
  height: 100%;
}
</style>