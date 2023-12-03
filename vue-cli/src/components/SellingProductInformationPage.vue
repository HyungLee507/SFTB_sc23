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
                  :src="image"
                  alt="Product Image"
                  class="product-image"
                  style="width: 250px; height: 250px; border-radius: 0;"
              />
              <b-button class="image-button" @click="editImage(index)">이미지 수정</b-button>
              <b-button class="delete-button" @click="deleteImage(index)">이미지 삭제</b-button>
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

.delete-button {
  background-color: rgb(207, 74, 86);
  margin-left: 30px;
  color: white;
}

.add-button {
  background-color: blue;
  margin-top: 30px;
  margin-bottom: 30px;
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
    const productId = this.$route.params.ProductId;

    axios.get(`/products/${productId}`)
        .then(response => {
          this.editedProduct = response.data;
        })
  },
  methods: {
    saveProductInformation() {
      // 서버에 저장하는 로직
    },
    editImage(index) {
      this.selectedImageIndex = index;
      this.$refs.fileInput.click();
    },
    addImage() {
      this.$refs.fileInput.click();
    },
    cancelProduct() {
      // 상품판매 취소
    },
    deleteImage(index) {
      if (this.isRepresentativeImage(index)) {
        alert("대표이미지는 삭제할 수 없습니다.");
      } else {
        this.editedProduct.images.splice(index, 1);
      }
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

