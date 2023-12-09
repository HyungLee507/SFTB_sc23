
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
              <b-button class="delete-button" @click="deleteImage(index)" :disabled="editedProduct.images.length <= 2">이미지 삭제</b-button>
            </div>
            <div>
              <input type="file" ref="fileInput" style="display: none"/>
              <b-button class="add-button" @click="addImage()" :disabled="editedProduct.images.length >= 5">이미지 추가</b-button>
            </div>
            <p>상품 이름:
              <b-input v-model="editedProduct.name"/>
            </p>
            <p>상품 가격:
              <b-input v-model="editedProduct.price" type="number" step="100"/>
            </p>
            <p>상품 카테고리:</p>
            <b-form-group>
              <b-row>
                <b-col>
                  <b-form-radio v-model="editedProduct.category" value="상의">상의</b-form-radio>
                </b-col>
                <b-col>
                  <b-form-radio v-model="editedProduct.category" value="아우터">아우터</b-form-radio>
                </b-col>
                <b-col>
                  <b-form-radio v-model="editedProduct.category" value="드레스">드레스</b-form-radio>
                </b-col>
                <b-col>
                  <b-form-radio v-model="editedProduct.category" value="운동복">운동복</b-form-radio>
                </b-col>
                <b-col>
                  <b-form-radio v-model="editedProduct.category" value="기타">기타</b-form-radio>
                </b-col>
              </b-row>
            </b-form-group>

          <b-form-group id="product-shoeSize" label="상품 사이즈">
          <b-form-radio-group v-model="editedProduct.shoeSize">
            <b-form-radio value="85">85</b-form-radio>
            <b-form-radio value="90">90</b-form-radio>
            <b-form-radio value="95">95</b-form-radio>
            <b-form-radio value="100">100</b-form-radio>
            <b-form-radio value="105">105</b-form-radio>
            <b-form-radio value="110">110</b-form-radio>
          </b-form-radio-group>
          </b-form-group>
            
            <p>상품 설명:
              <b-textarea v-model="editedProduct.description"/>
            </p>
            <b-button class=save-button @click="editText">저장</b-button>
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
    mounted() {
    const fileInput = this.$refs.fileInput;
    fileInput.addEventListener('change', () => {
      const file = fileInput.files[0];
      const formData = new FormData();
      formData.append('image', file);
      formData.append('itemId', this.editedProduct.id);

      axios.post('/add-image', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
        .then(response => {
          const productId = this.$route.params.productId;
          axios.get(`/product-detail/${productId}`)
            .then(response => {
              this.editedProduct = response.data;
            })
          console.log(response.data);
        })
        .catch(error => {
          // Handle error response
          console.error(error);
        });
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
    deleteImage(index) {
      const imageId = this.editedProduct.imageIds[index];
      axios
        .delete(`/delete-image/${imageId}`)
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
    },
    addImage() {
    this.$refs.fileInput.click();
    },
    cancelProduct() {
      const formData = new FormData();
      formData.append('itemId', this.editedProduct.id);
      axios.put(`/product-remove`,
      formData
      ).then(() => {
        alert('판매가 취소되었습니다.');
        this.$router.push('/user/mypage/saleshistory/selling');
      
      }).catch((error) => {
        console.log(error);
        alert('판매 취소에 실패했습니다.');
      })
    },
    isRepresentativeImage(index) {
      return index === 0;
    },
    editText(){
      const productId = this.$route.params.productId;
      axios.put(`/updateItem/text/${productId}`, {
        name: this.editedProduct.name,
        price: this.editedProduct.price,
        shoeSize: this.editedProduct.shoeSize,
        category: this.editedProduct.category,
        description: this.editedProduct.description,
        itemId: this.editedProduct.id,
      }).then(() => {
        alert('상품 정보가 수정되었습니다.');
        this.$router.push('/user/mypage/saleshistory/selling');
      
      }).catch((error) => {
        console.log(error);
        alert('상품 정보 수정에 실패했습니다.');
        
      });
    }
  },
};
</script>

