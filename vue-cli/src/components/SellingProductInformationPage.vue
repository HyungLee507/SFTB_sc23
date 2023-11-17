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
                    <button-list />
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
                            <input type="file" ref="fileInput" style="display: none" @change="handleImageUpload" />
                            <b-button class="add-button" @click="addImage">이미지 추가</b-button>
                        </div>
                        <p>상품 이름: <b-input v-model="editedProduct.name" /></p>
                        <p>상품 가격: <b-input v-model="editedProduct.price" type="number" step="100" /></p>
                        <p>상품 사이즈: <b-input v-model="editedProduct.showSize" type="number" step="5"/></p>
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
                        <p>상품 설명: <b-textarea v-model="editedProduct.description" /></p>
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
    padding-right: 50;
}
.image-button{
    margin-left: 50px;
}

.save-button{
    background-color:green;
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

<!-- <script>
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
        
        axios.get(`/products/${productId}`)   // api
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
                        this.selectedImageIndex = null; // 선택 인덱스 초기화
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
</script> -->

<script>
import ButtonList from "./ButtonList";

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

        if (productId === "1") {
            const testData1 = {
                images: [
                    "https://shopping-phinf.pstatic.net/main_1164266/11642661041.20191216104903.jpg?type=f300",
                    "https://shopping-phinf.pstatic.net/main_2955567/29555674431.20221013162919.jpg?type=f300",
                    "https://shopping-phinf.pstatic.net/main_2955567/29555674431.20221013162919.jpg?type=f300",
                ],
                name: "나이키운동화",
                price: 10000,
                showSize: 250,
                category: "운동화",
                description: "운동하기 편함",
                id: 1,
            };
            this.editedProduct = testData1;
        } else if (productId === "2") {
            const testData2 = {
                images: [
                    "https://shopping-phinf.pstatic.net/main_1164266/11642661041.20191216104903.jpg?type=f300",
                    "https://shopping-phinf.pstatic.net/main_2955567/29555674431.20221013162919.jpg?type=f300",
                    "https://shopping-phinf.pstatic.net/main_2955567/29555674431.20221013162919.jpg?type=f300",
                ],
                name: "아디다스 샌들",
                price: 15000,
                showSize: 270,
                category: "샌들",
                description: "편함",
                id: 2,
            };
            this.editedProduct = testData2;
        }
    },
    methods: {
        saveProductInformation() {
            // 서버에 저장하는 로직
        },
        cancelProduct() {
            // 상품판매 취소
        },
        editImage(index) {
            this.selectedImageIndex = index;
            this.$refs.fileInput.click();
        },
        addImage() {
            if (this.editedProduct.images.length < 5) {
                this.$refs.fileInput.click();
            } else {               
                alert("이미지는 최대 5개까지만 추가할 수 있습니다.");
            }
        },
        
        deleteImage(index) {
            if (this.isRepresentativeImage(index)) {
                alert("대표이미지는 삭제할 수 없습니다.");
            } else if (this.editedProduct.images.length > 3) {
                this.editedProduct.images.splice(index, 1);
            } else {               
                alert("이미지는 최소 3개까지 유지되어야 합니다.");
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

                    console.log("Edited product:", this.editedProduct);
                    this.$forceUpdate(); 
                };
                reader.readAsDataURL(file);
            }
        },
    },
};
</script>

