<template>
  <div class="commentpage">
    <b-container>
      <b-row>
        <b-col md="12">
          <h1 class="h1">후기관리</h1>
        </b-col>
        <div class="long-line"></div>
      </b-row>
      <b-row>
        <b-col md="3">
          <button-list />
        </b-col>
        <b-col md="9">
          <div v-if="sellerReviews.length === 0" class="content-container">
            <p style="font-weight: bold; font-size: 20px;">후기가 존재하지 않습니다.</p>
          </div>
          <div v-else class="content-container">
            <p style="font-weight: bold">판매자 후기 ({{ sellerReviews.length }})</p>
            <div v-for="(sellerReview, index) in sellerReviews.slice(0, showAllReviews ? sellerReviews.length : 5)"
              :key="index" class="review-container">
              <div class="reviewer-name">{{ maskReviewerName(sellerReview.reviewerName) }}</div>
              <div class="review-item">
                <p class="review-title">제목: {{ sellerReview.title }}</p>
                <p class="review-rating">
                  판매자 평점:
                  <span v-for="i in 5" :key="i" class="star-icon">
                    {{ i <= sellerReview.starRate ? '⭐' : '☆' }} </span>
                </p>
                <div class="review-content">
                  <p style="white-space: pre-line; word-break: break-all;">
                    <strong>{{ sellerReview.content }}</strong>
                  </p>
                </div>
                <div class="comment">
                  <div v-if="sellerReview.comments && sellerReview.comments.length > 0 && sellerReview.comments[0].content !== undefined">
                    <p style="white-space: pre-line; word-break: break-all; margin-top: 10px; margin-left: 10px; margin-right: 10px;">
                      ↳ 내 댓글: <span v-if="!sellerReview.editingComment && sellerReview.comments[0].content">{{ sellerReview.comments[0].content }}</span>
                      <span v-else></span>
                    </p>
                  </div>
                  <div v-else>
                    <p style="margin-top: 10px;">댓글을 작성하지 않았습니다.</p>
                  </div>
                </div>
              </div>
              <div v-if="!sellerReview.comments || sellerReview.comments.length === 0">
                <button class="modify-button" @click="toggleEditComment(index)">댓글 작성</button>
                <div v-if="sellerReview.editingComment">
                  <div class="text-area">
                    <textarea v-model="sellerReviews[index].comments.content" style="font-weight: bold; height: 150px; width: 100%;" @input="handleCommentInput(index)"></textarea>
                  </div>
                  <button class="save-cancel" @click="saveComment(index)"
                    style="margin-right: 10px; background-color: rgb(14, 109, 5);">저장</button>
                  <button class="save-cancel" @click="cancelEditComment(index)"
                    style="background-color: rgb(201, 53, 53);">취소</button>
                </div>
              </div>
            </div>
            <div class="review-button" v-if="sellerReviews.length > 5">
              <b-button class="show-all" @click="toggleShowAllReviews" variant="link">
                <p>{{ showAllReviews ? '숨기기' : '더 보기' }}</p>
              </b-button>
            </div>
            <div class="average-rating">
              <p>판매자 평균 평점: {{ calculateAverageRating().toFixed(1) }} / 5.0</p>
            </div>
          </div>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
import ButtonList from './ButtonList'
import axios from 'axios';

export default {

  components: {
    ButtonList
  },
  data() {
    return {
      sellerReviews: [],
      showAllReviews: false,

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

    axios.get("/reviews")
      .then((response) => {
        this.$set(this, 'sellerReviews', response.data.map(review => ({ ...review })));
        console.log(response.data);
      })
      .catch((error) => {
        console.error(error);
      });



  },
  methods: {

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
    toggleEditComment(index) {
      this.$set(this.sellerReviews[index], 'editingComment', !this.sellerReviews[index].editingComment);
    },
    saveComment(index) {
      const reviewId = this.sellerReviews[index].reviewId;
      const description = this.sellerReviews[index].comments.content;

      const data = new FormData();
      data.append("reviewId", reviewId);
      data.append("description", description);

      axios.post("/comment-upload", data, {
      })
        .then(() => {
          alert("댓글이 등록되었습니다.")
          this.$set(this.sellerReviews[index], 'editingComment', false);
          this.fetchReviews();
        })
        .catch(() => {
          alert("댓글 등록에 실패했습니다.")
          this.$set(this.sellerReviews[index], 'editingComment', false);
        });
    },
    cancelEditComment(index) {
      this.$set(this.sellerReviews[index], 'editingComment', false);
    },
    handleCommentInput(index) {
      if (!this.sellerReviews[index].comments) {
        this.$set(this.sellerReviews[index], 'comments', { content: '' });
      }
    },
    fetchReviews() {
      axios.get("/reviews")
          .then((response) => {
            this.$set(this, 'sellerReviews', response.data.map(review => ({ ...review })));
            console.log(response.data);
          })
          .catch((error) => {
            console.error(error);
          });
    },
  },
};
</script>

<style scoped>
.commentpage {
  margin-bottom: 300px;
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: bold;
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

.review-container {
  display: flex;
  flex-direction: column;
  border: 1px solid #000000d3;
  padding: 15px;
  margin-top: 10px;
  border-radius: 5px;
  margin-left: 180px;
  margin-right: 180px;
  min-height: 350px;
  min-width: 400px;
}

.comment {
  color: rgb(255, 255, 255);
  border: 1px solid rgba(0, 0, 0, 0.63);
  background-color: rgba(0, 0, 0, 0.712);
  border-radius: 5px;
  margin-top: 30px;
}

.modify-button {
  font-weight: bold;
  margin-top: 15px;
  margin-left: 100px;
  margin-right: 100px;
  border-radius: 5px;
}

.text-area {
  margin-top: 30px;
  margin-bottom: 15px;
  font-weight: bold;
}

.save-cancel {
  border-radius: 5px;
  font-weight: bold;
  border: 0px;
  color: white;
  width: 60px;
  height: 30px;
}
</style>