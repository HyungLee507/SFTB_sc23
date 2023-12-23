<template>
  <b-container class="productinformation">
    <h1 v-if="this.product.status !== 'FOR_SALE'" style="color: red; font-size: 2em;">판매완료된 상품입니다</h1>
    <h1 class="head1">{{ product.name }}</h1>
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
        <div class="long-line"></div>
        <h1 class="head1">{{ product.name }}</h1>
        <div class="product-info">
          <p style="margin-top: 20px;">판매자: <strong>{{ product.sellerName }}</strong></p>
          <p>가격: <strong>{{ product.price.toLocaleString() }}원</strong></p>
          <p>카테고리: <strong>{{ product.category }}</strong></p>
          <p>사이즈: <strong>{{ product.shoeSize }}</strong></p>
        </div>
        <p style="font-size: 22px; text-decoration: underline;"><strong>상품설명</strong></p>
        <div class="description-box" style="margin-bottom: 30px">
          <p><strong>{{ product.description }}</strong></p>
        </div>
        <p style="font-weight: bold">판매자 후기 ({{ sellerReviews.length }})</p>
        <div v-for="(sellerReview, index) in sellerReviews.slice(0, showAllReviews ? sellerReviews.length : 5)"
             :key="index" class="review-container">
          <div class="reviewer-name">{{ maskReviewerName(sellerReview.reviewerName) }}</div>
          <div class="review-item">
            <p class="review-title">제목: {{ sellerReview.title }}</p>
            <p class="review-rating">
              판매자 평점:
              <span v-for="i in 5" :key="i" class="star-icon">
        {{ i <= sellerReview.starRate ? '⭐' : '☆' }}
      </span>
            </p>
            <div class="review-content">
              <p style="white-space: pre-line; word-break: break-all;">
                <strong>{{ sellerReview.content }}</strong>
              </p>
            </div>
            <div v-if="sellerReview.comments.length > 0" class="comment">
              <p style="white-space: pre-line; word-break: break-all; margin-top: 10px; margin-left: 10px; margin-right: 10px;">
                ↳ 판매자 댓글: {{ sellerReview.comments[0].content }}
              </p>
            </div>
            <div v-else class="comment">
              <p style="white-space: pre-line; word-break: break-all; margin-top: 10px; margin-left: 10px; margin-right: 10px;">
                댓글이 없습니다.
              </p>
            </div>
          </div>
        </div>


        <div class="review-button" v-if="sellerReviews.length > 5">
          <b-button class="show-all" @click="toggleShowAllReviews" variant="link">
            {{ showAllReviews ? '숨기기' : '더 보기' }}
          </b-button>
        </div>
        <div class="average-rating">
          <p>판매자 평균 평점: {{ calculateAverageRating().toFixed(1) }} / 5.0</p>
        </div>
        <div class="buttons">
          <b-button variant="primary" @click="addToCart" style="margin-right: 30px">장바구니</b-button>
          <b-button variant="success" @click="openUploadPopup" style="margin-right: 30px">VR Fitting</b-button>
          <b-button variant="info" :to="'/product/payment/' + this.id">결제</b-button>
        </div>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
import axios from "axios";


export default {
  data() {
    return {
      product: {
        images: [],
        name: "",
        price: 0,
        sellerName: "",
        description: "",
        category: '',
        shoeSize: 0,
        status: '',
      },
      slide: 0,
      id: this.$route.params.id,
      showAllReviews: false,

      sellerReviews: [],

    };
  },


  created() {
    axios.interceptors.request.use((config) => {
      const accessToken = localStorage.getItem('accessToken');
      if (accessToken) {
        config.headers.Authorization = `Bearer ${accessToken}`;
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
    const id = this.$route.params.id;


    axios
        .get("/product-detail/" + id, {})
        .then((response) => {
          this.product = response.data;
        })
        .catch((error) => {
          console.log(error);
        });


    const productId = this.$route.params.id;

    axios.get("/seller-reviews/" + productId)
        .then((response) => {
          this.$set(this, 'sellerReviews', response.data.map(review => ({...review})));
        })
        .catch((error) => {
          console.error(error);
        });
  },


  methods: {
    getImageUrl(imageName) {
      return `/product/${imageName}`;
    },
    addToCart() {
      axios.post('/save-item?itemId=' + this.product.id, {})
          .then((response) => {
            console.log(response);
            alert('장바구니에 추가되었습니다.');
          })
          .catch((error) => {
            console.log(error);
            alert('장바구니 추가에 실패했습니다.')
          });
    },
    openUploadPopup() {
      const formData = new FormData();
      formData.append('productId', this.product.id);
      axios.post('/vf/productimg', formData)
          .then((response) => {
            const data = response.data;
            localStorage.setItem("dirname", data.dirname);
            localStorage.setItem("clothsrc", data.clothsrc);
            localStorage.setItem("hostUrl", data.hostUrl);
            // 업로드 팝업창 열기 로직 추가
            window.open('/product/upload/' + this.product.id, 'upload', 'width=500, height=500, left=100, top=50');
          })
          .catch((error) => {
            console.log(error);
            alert("에러로 인해 가상 피팅을 진행할 수 없습니다.")
          });
    },
    changeSlide(index) {
      this.slide = index;
    },
    calculateAverageRating() {
      if (this.sellerReviews.length === 0) {
        return 0;
      }

      const totalRating = this.sellerReviews.reduce((sum, review) => sum + review.starRate, 0);
      return totalRating / this.sellerReviews.length;
    },
    toggleShowAllReviews() {
      this.showAllReviews = !this.showAllReviews;
    },
    toggleShowAllContent(review) {
      review.showAllContent = !review.showAllContent;
    },
    toggleShowAllCommentContent(review) {
      review.showAllCommentContent = !review.showAllCommentContent;
    },
    maskReviewerName(name) {
      const firstChar = name.charAt(0);
      const lastChar = name.charAt(name.length - 1);

      if (name.length <= 2) {
        return firstChar + '*';
      } else {
        return firstChar + '*'.repeat(name.length - 2) + lastChar;
      }
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
  width: 120px;
  height: 180px;
  object-fit: cover;
  border: 1px solid #ddd;
}

.small-images img:hover {
  border: 1px solid #333;
}

.description-box {
  border: 2px solid #000000;
  padding: 15px;
  margin-top: 10px;
  border-radius: 5px;
  width: 800px;
  height: 300px;
  margin-left: auto;
  margin-right: auto;
}

.product-info {
  margin-left: 150px;
  margin-right: 150px;
  margin-bottom: 60px;
  font-size: 18px;
}


.star-icon {
  font-size: 20px;
  color: gold;
}

.review-container {
  display: flex;
  flex-direction: column;
  border: 1px solid #ddd;
  padding: 15px;
  margin-top: 10px;
  border-radius: 5px;
  margin-left: 180px;
  margin-right: 180px;
}

.review-item {
  margin-bottom: 15px;
}

.review-title {
  font-size: 18px;
  font-weight: bold;
}

.review-rating {
  font-size: 16px;
  color: gold;
}

.review-content {
  font-size: 16px;
  text-align: left;
}

.average-rating {
  margin-top: 10px;
  text-align: center;
  font-size: 18px;
  font-weight: bold;
}

.review-container {
  border: 5px solid #0000008a;
}

.review-button {
  margin-left: 200px;
  margin-right: 200px;
  margin-top: 20px;
}

.review-comment {
  border: 2px solid #F1B273EA;
  margin-left: 40px;
  margin-right: 40px;
}

.reviewer-name {
  font-size: 12px;
  margin-bottom: 5px;
}

.head1 {
  margin-bottom: 40px;
  margin-top: 40px;
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: bold;
}

.buttons {
  margin-bottom: 30px;
}

.show-all {
  background-color: white;
  color: #A26D45FF;
  border-color: white;
}

.productinformation {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: bold;
  margin-bottom: 200px;
}

.long-line {
  height: 3px;
  background-color: black;
  margin-top: 40px;
  margin-bottom: 40px;
  margin-left: 5%;
  width: 90%;
}

.carousel-slide img {
  max-width: 400px;
  max-height: 300px;
}

.carousel-item active {
  max-width: 400px;
  max-height: 300px;
}

.w-100 {
  width: 100% !important;
}

.d-block {
  display: block !important;
}

.img-fluid {
  max-width: 400px;
  max-height: 300px;
}

.comment {
  color: rgb(255, 255, 255);
  border: 1px solid rgba(0, 0, 0, 0.63);
  background-color: rgba(0, 0, 0, 0.712);
  border-radius: 5px;
  margin-top: 30px;
}
</style>