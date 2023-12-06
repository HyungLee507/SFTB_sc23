<template>
  <div class="productregistration">
    <p style="margin-top: 30px; font-size: 30px;">대표사진의 예시</p>
    <b-form @submit.prevent="submitForm">
      <div v-for="(image, index) in product.images" :key="index">
        <img class="representative" src="../assets/상품 대표사진.jpg" v-if="index === 0"/>
        <p v-if="index === 0">다음과 같이 대표사진을 등록해주세요. (이미지의 크기는 768x1024 혹은 3:4 비율을 권장합니다.) </p>
        <b-form-group :id="'product-image-' + index" :label="`상품 이미지${index === 0 ? ' [대표사진]' : ''}`"
                      style="font-weight: bold; margin-top: 50px; margin-left: 100px; margin-right: 100px;">
          <b-form-file @change="previewImage($event, index)" v-model="product.images[index]"
                      accept=".jpg"></b-form-file>
          <img class="preview" :src="product.imagePreviews[index]"
               v-if="product.imagePreviews[index] !== undefined && index === 0"/>
        </b-form-group>
      </div>
      <b-button class="add-image" @click="addImage" variant="success">사진 추가</b-button>
      <b-button class="delete-image" @click="removeImage" v-if="product.images.length > 1" variant="danger">사진
        제거
      </b-button>
      <b-form-group id="product-name" label="상품 이름" style="font-weight: bold; margin-left: 200px; margin-right: 200px;">
        <b-form-input v-model="product.name"></b-form-input>
      </b-form-group>
      <b-form-group id="product-category" label="상품 카테고리" style="font-weight: bold;">
        <b-form-radio-group v-model="product.category">
          <b-form-radio value="상의">상의</b-form-radio>
          <b-form-radio value="아우터">아우터</b-form-radio>
          <b-form-radio value="운동복">운동복</b-form-radio>
          <b-form-radio value="드레스">드레스</b-form-radio>
          <b-form-radio value="기타">기타</b-form-radio>

        </b-form-radio-group>
      </b-form-group>
      <b-form-group id="product-shoeSize" label="상품 사이즈" style="font-weight: bold;">
        <b-form-radio-group v-model="product.shoeSize">
          <b-form-radio value="85">85</b-form-radio>
          <b-form-radio value="90">90</b-form-radio>
          <b-form-radio value="95">95</b-form-radio>
          <b-form-radio value="100">100</b-form-radio>
          <b-form-radio value="105">105</b-form-radio>
          <b-form-radio value="110">110</b-form-radio>

        </b-form-radio-group>
      </b-form-group>

      <b-form-group id="product-price" label="상품 가격"
                    style="font-weight: bold; margin-left: 280px; margin-right: 280px;">
        <b-input-group>
          <b-form-input v-model="product.price" type="text" @input="formatPrice" min="0"></b-form-input>
        </b-input-group>
      </b-form-group>

      <b-form-group id="product-description" label="상품 설명"
                    style="font-weight: bold; margin-left: 150px; margin-right: 150px;">
        <b-form-textarea v-model="product.description"></b-form-textarea>
      </b-form-group>

      <b-button type="submit" variant="primary" style="margin-bottom: 40px;">등록</b-button>
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
      const token = localStorage.getItem('accessToken');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
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
  },
  methods: {
    previewImage(event, index) {
      const file = event.target.files[0];
      if (file && file.type === 'image/jpeg') {
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
      if (this.product.images.length < 2) {
        alert('이미지는 2개 이상 등록해주세요.');

        return;
      }
      const formData = new FormData();

      for (let i = 0; i < this.product.images.length; i++) {
        formData.append(`images[${i}]`, this.product.images[i]);
      }
      formData.append('name', this.product.name);
      formData.append('price', this.product.price);
      formData.append('description', this.product.description);
      formData.append('category', this.product.category);
      formData.append('shoeSize', this.product.shoeSize);

      axios.post('/product/item-upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
          .then(response => {
            console.log(response);
            alert('상품이 등록되었습니다.');
            this.$router.push('/product/list');
          })
          .catch(error => {
            console.log(error);
            alert('상품 등록에 실패했습니다.');
            this.$router.push('/product/list');
          });
    },
  },
};
</script>
<style scoped>
.preview {
  width: 250px;
  height: 250px;
  border-radius: 0;
  margin-top: 30px;
  font-weight: bold;
}

.representative {
  width: 250px;
  height: 250px;
  border-radius: 0;
  margin-top: 30px;
  font-weight: bold;
}

.add-image {
  margin-bottom: 30px;
  margin-right: 30px;
}

.delete-image {
  margin-bottom: 30px;
}

.form-group input[type="text"],
.form-group input[type="number"],
.form-group textarea,
.form-group .b-form-radio-group {
  width: calc(100% - 8rem);
  margin-right: 4rem;
  margin-left: 4rem;
  font-weight: bold;
}

.productregistration {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: bold;
  margin-bottom: 300px;
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