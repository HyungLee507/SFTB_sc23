<template>
  <div class="productlistpage">
    <b-form-input class="search" v-model="searchTerm" placeholder="이름으로 검색" @input="searchProducts"></b-form-input>
    <b-dropdown variant="outline-secondary" text="정렬" class="ml-2">
      <b-dropdown-item @click="sortBy('price', 'asc')">가격 (오름차순)</b-dropdown-item>
      <b-dropdown-item @click="sortBy('price', 'desc')">가격 (내림차순)</b-dropdown-item>
      <b-dropdown-item @click="sortBy('name', 'asc')">상품 이름 (오름차순)</b-dropdown-item>
      <b-dropdown-item @click="sortBy('name', 'desc')">상품 이름 (내림차순)</b-dropdown-item>
      <b-dropdown-item @click="sortBy('createdAt', 'asc')">상품 등록일 (오름차순)</b-dropdown-item>
      <b-dropdown-item @click="sortBy('createdAt', 'desc')">상품 등록일 (내림차순)</b-dropdown-item>
    </b-dropdown>
    <div class="long-line"></div>
    <b-card-group>
      <router-link v-for="product in sortedProducts" :key="product.id" :to="'/product-list/' + product.id"
                   style="margin-bottom: 3rem;">
        <b-card :title="product.name" :img-src="getImageUrl(product.image)" img-alt="Image" img-top
                class="fixed-card-size mx-3 mb-3">
          <b-card-text>{{ formatPrice(product.price) }}원</b-card-text>
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
      products: [],
      searchTerm: '',
      sortKey: '',
      sortOrder: '',
    };
  },

  created() {
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
          .get('/product-list')
          .then((response) => {
            this.products = response.data;

          })
          .catch((error) => {
            console.log(error);
          });
    },
    searchProducts() {
      this.filteredProducts = this.products.filter((product) => product.name.includes(this.searchTerm));
    },
    sortBy(key, order) {
      this.sortKey = key;
      this.sortOrder = order;
    },
    getImageUrl(imageName) {
      return `/product/${imageName}`;
    },
    formatPrice(price) {
      return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    },
  },
  computed: {
    sortedProducts() {
      let sorted = [...this.filteredProducts];
      if (this.sortKey) {
        sorted.sort((a, b) => {
          if (this.sortKey === 'price') {
            return this.sortOrder === 'asc' ? a[this.sortKey] - b[this.sortKey] : b[this.sortKey] - a[this.sortKey];
          } else if (this.sortKey === 'name') {
            return this.sortOrder === 'asc' ? a[this.sortKey].localeCompare(b[this.sortKey]) : b[this.sortKey].localeCompare(a[this.sortKey]);
          } else if (this.sortKey === 'createdAt') {
            return this.sortOrder === 'asc' ? new Date(a[this.sortKey]) - new Date(b[this.sortKey]) : new Date(b[this.sortKey]) - new Date(a[this.sortKey]);
          }
          return 0;
        });
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

.productlistpage {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: bold;
  margin-bottom: 300px;
}

.fixed-card-size {
  color: black;
}

.ml-2 {
  margin-bottom: 40px;
}

.search {
  margin-bottom: 20px;
}

.long-line {
  height: 3px;
  background-color: black;
  margin-top: 20px;
  margin-bottom: 40px;
  margin-left: 3%;
  width: 94%;
}
</style>




