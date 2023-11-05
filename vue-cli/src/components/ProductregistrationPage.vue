<template>
    <div>
        <b-form @submit.prevent="submitForm">
            <div v-for="(image, index) in product.images" :key="index">
                <b-form-group :id="'product-image-' + index" label="상품 이미지">
                    <b-form-file v-model="product.images[index]" accept="image/*"></b-form-file>
                </b-form-group>
            </div>
            <b-button @click="addImage">이미지 추가</b-button>
            <b-button @click="removeImage" v-if="product.images.length > 0">이미지 제거</b-button>

            <b-form-group id="product-name" label="상품 이름">
                <b-form-input v-model="product.name"></b-form-input>
            </b-form-group>

            <b-form-group id="product-price" label="상품 가격">
                <b-input-group>
                    <b-input-group-prepend is-text>$</b-input-group-prepend>
                    <b-form-input v-model="product.price" type="number" min="0"></b-form-input>
                </b-input-group>
            </b-form-group>

            <b-form-group id="product-description" label="상품 설명">
                <b-form-textarea v-model="product.description"></b-form-textarea>
            </b-form-group>

            <b-button type="submit" variant="primary">등록</b-button>
        </b-form>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            product: {
                images: [],
                name: '',
                price: 0,
                description: ''
            }
        }
    },
    methods: {
        addImage() {
            this.product.images.push('');
        },
        removeImage() {
            this.product.images.pop();
        },
        submitForm() {
            axios.post('/api/products', this.product)
                .then(response => {
                    console.log(response.data);
                })
                .catch(error => {
                    console.log(error);
                });
        }
    }
}
</script>
