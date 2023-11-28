<template>
  <b-container>
    <h1 style="margin-top: 30px;">{{ product.name }}</h1>
    <b-row>
      <b-col>
        <b-carousel
            id="carousel-1"
            v-model="slide"
            :interval="0"
            controls
            indicators
            background="#ABABAB"
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
        <div class="small-images">
          <img
              v-for="(image, index) in product.images"
              :key="index"
              :src="getImageUrl(image)"
              :alt="'작은 상품 이미지 ' + (index + 1)"
              @click="changeSlide(index)"
          />
        </div>
        <h1>{{ product.name }}</h1>
        <div class="product-info">
          <p>판매자: <strong>{{ product.sellerName }}</strong></p>
          <p>가격: <strong>{{ product.price.toLocaleString() }}원</strong></p>
          <p>카테고리: <strong>{{ product.category }}</strong></p>
          <p>사이즈: <strong>{{ product.shoeSize }}</strong></p>
          <p>상품설명</p>
          <div class="description-box" style="margin-bottom: 30px">
            <p><strong>{{ product.description }}</strong></p>
          </div>
        </div>
        <b-button variant="primary" @click="addToCart" style="margin-right: 30px">장바구니</b-button>
        <b-button variant="success" @click="openUploadPopup" style="margin-right: 30px">VR Fitting</b-button>
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
    },
    openUploadPopup() {
      // 업로드 팝업창 열기 로직 추가
      window.open('/product/upload/'+ this.product.id, 'upload', 'width=500, height=500, left=100, top=50');
      const formData = new FormData();
      formData.append('productId', this.product.id);
      axios.post('/vf/productimg', formData)
          .then((response) => {
            console.log(response.data);
          })
          .catch((error) => {
            console.log(error);
          });
    },
    changeSlide(index) {
      this.slide = index;
    },
  },
};
</script>

<style scoped>
.small-images {
  display: flex;
  justify-content: center;
  margin-top: 10px;
}

.small-images img {
  cursor: pointer;
  width: 180px;
  height: 120px;
  object-fit: cover;
  border: 1px solid #ddd;
}

.small-images img:hover {
  border: 1px solid #333;
}

.description-box {
  border: 1px solid #ddd;
  padding: 15px;
  margin-top: 10px;
  border-radius: 5px;
  width: 800px;
  height:300px;
  margin-left: auto;
  margin-right: auto;
}
</style>