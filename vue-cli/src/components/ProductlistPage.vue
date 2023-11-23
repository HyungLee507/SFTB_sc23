<!--<template>-->
<!--  <div>-->
<!--    <b-form-input v-model="searchTerm" placeholder="Search by name" @input="searchProducts"></b-form-input>-->
<!--    <b-dropdown variant="outline-secondary" text="Sort by" class="ml-2">-->
<!--      <b-dropdown-item @click="sortBy('price')">Price</b-dropdown-item>-->
<!--      <b-dropdown-item @click="sortBy('name')">Name</b-dropdown-item>-->
<!--      <b-dropdown-item @click="sortBy('createdAt')">Date</b-dropdown-item>-->
<!--    </b-dropdown>-->
<!--    <b-card-group deck>-->
<!--      <router-link v-for="product in sortedProducts" :key="product.id" :to="'/product-list/' + product.id">-->
<!--        <b-card :title="product.name" :img-src="getImageUrl(product.image)" img-alt="Image" img-top>-->
<!--          <b-card-text>{{ product.price }}</b-card-text>-->
<!--        </b-card>-->
<!--      </router-link>-->
<!--    </b-card-group>-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--import axios from 'axios';-->

<!--export default {-->
<!--  data() {-->
<!--    return {-->
<!--      products: [], // 상품정보를 저장할 배열-->
<!--      searchTerm: '', // 검색어를 저장할 변수-->
<!--      sortKey: '', // 정렬 기준을 저장할 변수-->
<!--    };-->
<!--  },-->
<!--  created() {-->
<!--    this.fetchProducts();-->
<!--  },-->
<!--  methods: {-->
<!--    fetchProducts() {-->
<!--      axios-->
<!--          .get('/product-list')-->
<!--          .then((response) => {-->
<!--            this.products = response.data;-->
<!--            console.log(response.data);-->
<!--          })-->
<!--          .catch((error) => {-->
<!--            console.log(error);-->
<!--          });-->
<!--    },-->
<!--    searchProducts() {-->
<!--      // 검색어에 따라 필터링된 상품 목록을 업데이트합니다.-->
<!--      this.filteredProducts = this.products.filter((product) => product.name.includes(this.searchTerm));-->
<!--    },-->
<!--    sortBy(key) {-->
<!--      this.sortKey = key;-->
<!--    },-->
<!--    getImageUrl(imageName) {-->
<!--      return `http://localhost:8080/product/${imageName}`;-->
<!--      //return `https://fc96-211-219-14-170.ngrok-free.app/product/${imageName}`;-->
<!--    }-->
<!--  },-->
<!--  computed: {-->
<!--    sortedProducts() {-->
<!--      let sorted = [...this.filteredProducts];-->
<!--      if (this.sortKey) {-->
<!--        sorted.sort((a, b) => {-->
<!--          if (this.sortKey === 'price') {-->
<!--            return a[this.sortKey] - b[this.sortKey];-->
<!--          } else if (this.sortKey === 'name') {-->
<!--            return a[this.sortKey].localeCompare(b[this.sortKey]);-->
<!--          } else if (this.sortKey === 'createdAt') {-->
<!--            return new Date(a[this.sortKey]) - new Date(b[this.sortKey]);-->
<!--          }-->
<!--        });-->
<!--      }-->
<!--      return sorted;-->
<!--    },-->
<!--    filteredProducts() {-->
<!--      if (this.searchTerm) {-->
<!--        return this.products.filter((product) => product.name.includes(this.searchTerm));-->
<!--      } else {-->
<!--        return this.products;-->
<!--      }-->
<!--    },-->
<!--  },-->
<!--};-->
<!--</script>-->

<!--<style>-->
<!--.b-card {-->
<!--  max-width: 20rem;-->
<!--  margin-bottom: 1rem;-->
<!--}-->
<!--</style>-->

<template>
  <div>
    <b-form-input v-model="searchTerm" placeholder="Search by name" @input="searchProducts"></b-form-input>
    <b-dropdown variant="outline-secondary" text="Sort by" class="ml-2">
      <b-dropdown-item @click="sortBy('price', 'asc')">가격 (오름차순)</b-dropdown-item>
      <b-dropdown-item @click="sortBy('price', 'desc')">가격 (내림차순)</b-dropdown-item>
      <b-dropdown-item @click="sortBy('name', 'asc')">상품 이름 (오름차순)</b-dropdown-item>
      <b-dropdown-item @click="sortBy('name', 'desc')">상품 이름 (내림차순)</b-dropdown-item>
      <b-dropdown-item @click="sortBy('createdAt', 'asc')">상품 등록일 (오름차순)</b-dropdown-item>
      <b-dropdown-item @click="sortBy('createdAt', 'desc')">상품 등록일 (내림차순)</b-dropdown-item>
    </b-dropdown>
    <b-card-group deck>
      <router-link v-for="product in sortedProducts" :key="product.id" :to="'/product-list/' + product.id">
        <b-card :title="product.name" :img-src="getImageUrl(product.image)" img-alt="Image" img-top
                class="fixed-card-size">
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
      sortOrder: '', // 정렬 순서를 저장할 변수
    };
  },
  created() {
    this.fetchProducts();
  },
  methods: {
    fetchProducts() {
      axios
          .get('/product-list')
          .then((response) => {
            this.products = response.data;
            console.log(response.data);
          })
          .catch((error) => {
            console.log(error);
          });
    },
    searchProducts() {
      // 검색어에 따라 필터링된 상품 목록을 업데이트합니다.
      this.filteredProducts = this.products.filter((product) => product.name.includes(this.searchTerm));
    },
    sortBy(key, order) {
      this.sortKey = key;
      this.sortOrder = order;
    },
    getImageUrl(imageName) {
      return `http://localhost:8080/product/${imageName}`;
      // return `https://c6d8-14-63-41-207.ngrok-free.app/product/${imageName}`;
    }
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
          return 0; // 추가된 부분
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
</style>




