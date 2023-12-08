<template>
  <div class="sellingproductinformation">
    <b-container>
      <br>
      <b-row>
        <b-col md="12">
          <h1>판매상품 정보 수정</h1>
        </b-col>
      </b-row>
      <b-row>
        <b-col md="3">
          <button-list/>
        </b-col>
        <b-col md="9">
          <div>
            <p style="margin-right: 300px;">[대표이미지]</p>
            <div v-for="(image, index) in editedProduct.images" :key="index">
              <img
                  :src="getImageUrl(image)"
                  alt="Product Image"
                  class="product-image"
                  style="width: 250px; height: 250px; border-radius: 0;"
              />
              <input type="file" ref="fileEdit" style="display: none" />
              <b-button class="image-button" @click="editImage(index)">이미지 수정</b-button>
            </div>
            <div>
              <input type="file" ref="fileInput" style="display: none" @change="handleImageUpload"/>
              <b-button class="add-button" @click="addImage">이미지 추가</b-button>
            </div>
            <p>상품 이름:
              <b-input v-model="editedProduct.name"/>
            </p>
            <p>상품 가격:
              <b-input v-model="editedProduct.price" type="number" step="100"/>
            </p>
            <p>상품 사이즈:
              <b-input v-model="editedProduct.shoeSize" type="number" step="5"/>
            </p>
            <p>상품 카테고리:</p>
            <b-form-group>
              <b-row>
                <b-col>
                  <b-form-radio v-model="editedProduct.category" value="운동화">운동화</b-form-radio>
                </b-col>
                <b-col>
                  <b-form-radio v-model="editedProduct.category" value="단화">단화</b-form-radio>
                </b-col>
                <b-col>
                  <b-form-radio v-model="editedProduct.category" value="캐주얼">캐주얼</b-form-radio>
                </b-col>
                <b-col>
                  <b-form-radio v-model="editedProduct.category" value="스포츠">스포츠</b-form-radio>
                </b-col>
                <b-col>
                  <b-form-radio v-model="editedProduct.category" value="기타">기타</b-form-radio>
                </b-col>
              </b-row>
            </b-form-group>
            <p>상품 설명:
              <b-textarea v-model="editedProduct.description"/>
            </p>
            <b-button class=save-button @click="saveProductInformation">저장</b-button>
            <b-button @click="cancelProduct">판매취소</b-button>
          </div>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<style scoped>
.product-image {
  width: 200px;
  height: auto;
  padding-right: 50px;
}

.image-button {
  margin-left: 50px;
}

.save-button {
  background-color: green;
  margin-right: 50px;
}


.add-button {
  background-color: blue;
  margin-top: 30px;
  margin-bottom: 30px;
}

.sellingproductinformation {
  margin-bottom: 200px;
}
</style>



<script>
import ButtonList from './ButtonList';
import axios from 'axios';

export default {
  components: {
    ButtonList,
  },
  data() {
    return {
      editedProduct: {
        images: [],
        imagePreviews: [],
        name: "",
        price: 0,
        showSize: 0,
        category: "",
        description: "",
        id: 0,
      },
      selectedImageIndex: null,
    };
  },
  created() {
    const productId = this.$route.params.productId;

    axios.get(`/product-detail/${productId}`)
        .then(response => {
          this.editedProduct = response.data;
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
    saveProductInformation() {
      // 서버에 저장하는 로직
    },
        getImageUrl(imageName) {
      return `/product/${imageName}`;
    },
    editImage(index) {
      const imageId = this.editedProduct.imageIds[index];
      const fileEdit = this.$refs.fileEdit[index];
      console.log(this.$refs.fileEdit);
      fileEdit.click();
      fileEdit.onchange = () => {
        const file = fileEdit.files[0];
        const formData = new FormData();
        formData.append("file", file);
        axios
          .put(`/updateItem/image/${imageId}`, formData)
          .then(() => {
              const productId = this.$route.params.productId;
              axios.get(`/product-detail/${productId}`)
              .then(response => {
                this.editedProduct = response.data;
              })
          
          })
          .catch((error) => {
            console.log(error);
          });
      };
    },
    addImage() {
      this.$refs.fileInput.click();
    },
    cancelProduct() {
      // 상품판매 취소
    },
    isRepresentativeImage(index) {
      return index === 0;
    },
    handleImageUpload(event) {
      const file = event.target.files[0];

      if (file) {
        const reader = new FileReader();
        reader.onload = () => {
          if (!this.editedProduct.images) {
            this.$set(this.editedProduct, "images", []);
          }
          if (this.selectedImageIndex !== null) {
            this.editedProduct.images[this.selectedImageIndex] = reader.result;
            this.selectedImageIndex = null;
          } else {
            this.editedProduct.images.push(reader.result);
          }
          this.$forceUpdate();
        };
        reader.readAsDataURL(file);
      }
    },
  },
};
</script>

