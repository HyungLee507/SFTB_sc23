<!--<template>-->
<!--  <div>-->
<!--    <b-form @submit.prevent="submitForm">-->
<!--      <div v-for="(image, index) in product.images" :key="index">-->
<!--        <b-form-group :id="'product-image-' + index" label="상품 이미지">-->
<!--          <b-form-file v-model="product.images[index]" accept="image/*"></b-form-file>-->
<!--        </b-form-group>-->
<!--      </div>-->
<!--      <b-button @click="addImage">이미지 추가</b-button>-->
<!--      <b-button @click="removeImage" v-if="product.images.length > 0">이미지 제거</b-button>-->

<!--      <b-form-group id="product-name" label="상품 이름">-->
<!--        <b-form-input v-model="product.name"></b-form-input>-->
<!--      </b-form-group>-->

<!--      <b-form-group id="product-price" label="상품 가격">-->
<!--        <b-input-group>-->
<!--          <b-input-group-prepend is-text>$</b-input-group-prepend>-->
<!--          <b-form-input v-model="product.price" type="number" min="0"></b-form-input>-->
<!--        </b-input-group>-->
<!--      </b-form-group>-->

<!--      <b-form-group id="product-description" label="상품 설명">-->
<!--        <b-form-textarea v-model="product.description"></b-form-textarea>-->
<!--      </b-form-group>-->

<!--      <b-button type="submit" variant="primary">등록</b-button>-->
<!--    </b-form>-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--import axios from 'axios';-->

<!--export default {-->
<!--  data() {-->
<!--    return {-->
<!--      product: {-->
<!--        uploadFiles: [],-->
<!--        name: '',-->
<!--        price: 0,-->
<!--        description: ''-->
<!--      }-->
<!--    }-->
<!--  },-->
<!--  methods: {-->
<!--    addImage() {-->
<!--      this.product.images.push('');-->
<!--    },-->
<!--    removeImage() {-->
<!--      this.product.images.pop();-->
<!--    },-->
<!--    submitForm() {-->
<!--      axios.post('/product/item-upload', this.product)-->
<!--          .then(response => {-->
<!--            console.log(response.data);-->
<!--          })-->
<!--          .catch(error => {-->
<!--            console.log(error);-->
<!--          });-->
<!--    }-->
<!--  }-->
<!--}-->
<!--</script>-->

<template>
  <div>
    <b-form @submit.prevent="submitForm">
      <div v-for="(image, index) in product.images" :key="index">
        <b-form-group :id="'product-image-' + index" label="상품 이미지">
          <b-form-file @change="previewImage($event, index)" v-model="product.images[index]"
                       accept="image/*"></b-form-file>
          <img class="preview" :src="product.imagePreviews[0]"
               v-if="product.imagePreviews[0] !== undefined && index === 0"/>
        </b-form-group>
      </div>
      <b-button @click="addImage">이미지 추가</b-button>
      <b-button @click="removeImage" v-if="product.images.length > 0">이미지 제거</b-button>

      <b-form-group id="product-name" label="상품 이름">
        <b-form-input v-model="product.name"></b-form-input>
      </b-form-group>
      <b-form-group id="product-category" label="상품 카테고리">
        <b-form-radio-group v-model="product.category">
          <b-form-radio value="운동화">운동화</b-form-radio>
          <b-form-radio value="단화">단화</b-form-radio>
          <b-form-radio value="캐주얼">캐주얼</b-form-radio>
          <b-form-radio value="스포츠">스포츠</b-form-radio>
          <b-form-radio value="기타">기타</b-form-radio>
        </b-form-radio-group>
      </b-form-group>

      <b-form-group id="product-shoeSize" label="상품 사이즈">
        <b-form-input v-model="product.shoeSize" type="number"></b-form-input>
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
        images: [undefined],
        sellerName: '',
        imagePreviews: [],
        name: '',
        price: 0,
        category: '',
        shoeSize: 0,
        description: '',
      },
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
  },
  methods: {
    previewImage(event, index) {
      const file = event.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = e => {
          this.$set(this.product.imagePreviews, index, e.target.result);
        };
        reader.readAsDataURL(file);
      } else {
        this.$set(this.product.imagePreviews, index, undefined);
      }
    },
    addImage() {
      this.product.images.push(undefined);
    },
    removeImage() {
      this.product.images.pop();
      this.product.imagePreviews.splice(-1, 1);
    },
    submitForm() {
      if (this.product.images.length < 3) {
        alert('이미지는 3개 이상 등록해주세요.');

        return;
      }
      const formData = new FormData();

      // 이미지와 다른 상품 정보를 FormData 객체에 추가
      for (let i = 0; i < this.product.images.length; i++) {
        formData.append(`images[${i}]`, this.product.images[i]);
      }
      formData.append('name', this.product.name);
      formData.append('price', this.product.price);
      formData.append('sellerName', this.product.sellerName);
      formData.append('category', this.product.category);
      formData.append('shoeSize', this.product.shoeSize);
      formData.append('description', this.product.description);

      // FormData 객체를 서버에 제출
      axios.post('/product/item-upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
          .then(response => {
            console.log(response);
          })
          .catch(error => {
            console.log(error);
          });
    },
  },
};
</script>
<style scoped>
.preview {
  width: 100px;
  height: 100px;
  border-radius: 50%;
}
</style>