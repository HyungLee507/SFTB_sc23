<template>
  <div class="reviewpage">
    <h1 class="text-center" style="margin-top: 30px">상품 리뷰</h1>
    <form @submit.prevent="saveReview" class="p-4 d-flex flex-column align-items-center">
      <div class="form-group w-60">
        <label for="title" style="font-weight: bold">제목:</label>
        <input style="font-weight: bold;"  type="text" id="title" v-model="review.title" required class="form-control">
      </div>
      <div class="form-group w-70">
        <label for="rating" style="font-weight: bold">평가:</label>
        <star-rating
            id="rating"
            v-model="review.starRate"
            required
            :show-rating="false"
            :star-size="30"
        ></star-rating>
      </div>
      <div class="form-group w-50">
        <label for="content" style="font-weight: bold">내용:</label>
        <textarea style="font-weight: bold;" id="content" v-model="review.content" required class="form-control"></textarea>
      </div>
      <button type="submit" class="btn btn-primary">저장</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios';
import StarRating from 'vue-star-rating';

export default {
  components: {
    StarRating,
  },
  data() {
    return {
      review: {
        itemId: 0,
        title: '',
        starRate: 0,
        content: '',
      },
    };
  },
  created(){
        axios.interceptors.response.use((config) => {
      return config;
    }, function (error) {
      if (error.response && error.response.status === 401) {
        alert('로그인이 필요합니다.');
        this.$router.push('/account/login');
      }
      return Promise.reject(error);
    });
  },

  methods: {
    saveReview() {
      this.review.itemId = this.$route.params.id;
      axios
          .post("/review-upload", {
            itemId: this.review.itemId,
            title: this.review.title,
            starRate: this.review.starRate,
            content: this.review.content,

          })
          .then(() => {
            alert('후기가 저장되었습니다!');
            this.$router.push({name: 'productinformationPage', params: {id: this.$route.params.id}});
          })
          .catch(() => {
            alert('후기 저장에 실패했습니다!');
          })
    },
  },
};
</script>

<style scoped>
.text-center {
  margin-bottom: 40px; 
  margin-top: 40px;
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: bold;
}

.reviewpage {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: bold;
}
</style>
