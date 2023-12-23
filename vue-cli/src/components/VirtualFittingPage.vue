<template>
  <div class="virtualfittingpage">
    <b-container class="full-container">
      <b-row>
        <b-col md="12">
          <h1 class="h1">가상 피팅</h1>
        </b-col>
        <div class="long-line"></div>
      </b-row>
      <b-row>
        <b-col md="3">
          <button-list/>
        </b-col>
        <b-col md="9">
          <div>
            <b-card-group deck>
              <div v-for="styleShot in styleShots" :key="styleShot.styleShotId">
                <b-card :title="styleShot.itemName" :img-src="getImageUrl(styleShot.styleShotName)" img-alt="Image"
                        img-top class="fixed-card-size" @click="openModal(styleShot)">
                </b-card>
              </div>
            </b-card-group>
          </div>
        </b-col>
      </b-row>
    </b-container>
    <b-modal v-model="modalShow" @ok="deleteProduct(styleShotId)" @cancel="redirectToProductList(itemId)">
      <template v-slot:modal-title>
        삭제 또는 상품페이지로 이동
      </template>
      <template v-slot:default>
        <p>카드를 삭제하거나 상품페이지로 이동하시겠습니까?</p>
      </template>
      <template v-slot:modal-footer>
        <b-button variant="danger" size="sm" @click="deleteProduct(styleShotId)">삭제</b-button>
        <b-button size="sm" @click="redirectToProductList(itemId)">상품페이지로 이동</b-button>
      </template>
    </b-modal>
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
      styleShots: [],
      modalShow: false,
      styleShotId: null,
      itemId: null,
    };
  },
  created() {
    this.item_id = this.$route.params.id;
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
      // return `https://vfmarket.store/vf/${imageName}`;
    },
    openModal(styleShot) {
      this.styleShotId = styleShot.styleShotId;
      this.itemId = styleShot.itemId;
      this.modalShow = true;
    },
    deleteProduct(styleShotId) {
      axios
          .delete(`/vf/deletestyle?styleShotId=${styleShotId}`)
          .then((response) => {
            console.log(response.data);
            this.fetchProducts();
            this.modalShow = false;
          })
          .catch((error) => {
            console.log(error);
          });
    },
    redirectToProductList(itemId) {
      this.$router.push(`/product-list/${itemId}`);
      this.modalShow = false;
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

.virtualfittingpage {
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
</style>