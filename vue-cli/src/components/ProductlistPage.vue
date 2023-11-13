<template>
    <div>
        <b-form-input v-model="searchTerm" placeholder="Search by name"></b-form-input>
        <b-dropdown variant="outline-secondary" text="Sort by" class="ml-2">
            <b-dropdown-item @click="sortBy('price')">Price</b-dropdown-item>
            <b-dropdown-item @click="sortBy('name')">Name</b-dropdown-item>
            <b-dropdown-item @click="sortBy('createdAt')">Date</b-dropdown-item>
        </b-dropdown>
        <b-card-group deck>
            <router-link v-for="product in sortedProducts" :key="product.id" :to="'/product/list/' + product.id">
                <b-card :title="product.name" img-src="product.images[0]" img-alt="Image" img-top>
                    <b-card-text>{{ product.price }}</b-card-text>
                </b-card>
            </router-link>
        </b-card-group>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            products: [], // 상품정보를 저장할 배열
            searchTerm: '', // 검색어를 저장할 변수
            sortKey: '', // 정렬 기준을 저장할 변수
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

        axios
            .get('/api/products')
            .then((response) => {
                this.products = response.data;
            })
            .catch((error) => {
                console.log(error);
            });
    },
    computed: {
        sortedProducts() {
            let sorted = [...this.products];
            if (this.sortKey) {
                sorted.sort((a, b) => a[this.sortKey].localeCompare(b[this.sortKey]));
            }
            return sorted;
        },
        filteredProducts() {
            if (this.searchTerm) {
                return this.products.filter((product) => product.name.includes(this.searchTerm));
            } else {
                return this.products;
            }
        },
    },
    methods: {
        sortBy(key) {
            this.sortKey = key;
        },
    },
};
</script>

<style scoped>
.b-card {
    max-width: 20rem;
    margin-bottom: 1rem;
}
</style>






