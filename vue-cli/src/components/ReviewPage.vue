
<template>
    <div>
        <h1 class="text-center">후기 작성</h1>
        <form @submit.prevent="saveReview" class="p-4">
            <div class="form-group">
                <label for="title">제목:</label>
                <input type="text" id="title" v-model="review.title" required class="form-control">
            </div>
            <div class="form-group">
                <label for="score">판매자에 대한 점수:</label>
                <select id="score" v-model="review.score" required class="form-control">
                    <option value="1">1점</option>
                    <option value="2">2점</option>
                    <option value="3">3점</option>
                    <option value="4">4점</option>
                    <option value="5">5점</option>
                </select>
            </div>
            <div class="form-group">
                <label for="content">내용:</label>
                <textarea id="content" v-model="review.content" required class="form-control"></textarea>
            </div>
            <button type="submit" class="btn btn-primary">저장</button>
        </form>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            review: {
                title: '',
                score: '',
                content: ''
            }
        };
    },
    methods: {
        saveReview() {
            axios
                .post('/api/review', {
                    title: this.review.title,
                    score: this.review.score,
                    content: this.review.content,
                    item_id: this.$route.params.id
                })
                .then(response => {
                    alert('후기가 저장되었습니다!');
                    this.$router.push({ name: 'productinformationPage', params: { id: this.$route.params.id } });
                    console.log(response.data);
                })
                .catch(error => {
                    alert('후기 저장에 실패했습니다!');
                    console.error(error);
                });
        }
    }
};
</script>

