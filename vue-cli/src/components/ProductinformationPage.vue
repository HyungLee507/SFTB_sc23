<template>
  <b-container>
    <b-row>
      <b-col>
        <b-carousel
            id="carousel-1"
            v-model="slide"
            :interval="4000"
            controls
            indicators
            background="#ABABAB"
            img-width="1024"
            img-height="480"
            style="text-shadow: 1px 1px 2px #333;"
        >
          <b-carousel-slide
              v-for="(image, index) in product.images"
              :key="index"
              :img-src="getImageUrl(image)"
              :alt="'Product image ' + (index + 1)"
          ></b-carousel-slide>
        </b-carousel>
      </b-col>
    </b-row>
    <b-row>
      <b-col>
        <h1>{{ product.name }}</h1>
        <p>{{ product.sellerName }}</p>
        <p>{{ product.price }}</p>
        <p>{{ product.category }}</p>
        <p>{{ product.shoeSize }}</p>
        <p>{{ product.description }}</p>
        <b-button variant="primary" @click="addToCart">장바구니</b-button>
        <b-button variant="success" to="#">VR Fitting</b-button>
        <!--        <b-button variant="info" to="#">결제</b-button>-->
        <b-button variant="info" :to="'/product/payment/' + this.id">결제</b-button>
      </b-col>
    </b-row>
  </b-container>
</template>
<script>
import axios from "axios";

export default {
  data() {
    return {
      product: {//상품정보를 저장할 객체
        images: [],
        name: "",
        price: 0,
        sellerName: "",
        description: "",
        category: '',
        shoeSize: 0,
      },
      slide: 0,
      id: this.$route.params.id,
    };
  },
  created() {
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
    const id = this.$route.params.id;//상품의 id를 받아옴
    axios
        .get("/product-list/" + id) //상품정보를 받아오는 api호출
        .then((response) => {
          this.product = response.data;//상품정보를 받아옴
        })
        .catch((error) => {
          console.log(error);
        });
  },
  methods: {
    getImageUrl(imageName) {
      return `http://localhost:8080/product/${imageName}`;
      // return `https://c6d8-14-63-41-207.ngrok-free.app/product/${imageName}`;
    },
    addToCart() {
      // axios.post('/save-item', {
      //   productId: this.product.id,
      // })
      axios.post('/save-item?itemId=' + this.product.id, {})
          .then((response) => {
            console.log(response.data);
          })
          .catch((error) => {
            console.log(error);
          });
    }
  },
};
</script>