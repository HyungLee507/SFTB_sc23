<template>
    <div class="virtualfittingpage">
        <b-container>
            <b-row>
                <b-col md="12">
                    <h1 style="margin-bottom: 40px; margin-top: 40px;">가상 피팅</h1>
                </b-col>
            </b-row>
            <b-row>
                <b-col md="3">
                    <button-list />
                </b-col>
                <b-col md="9">
                    <h3>가상피팅할 내사진</h3>
                    <div v-for="(image, index) in beforeVrfitting.images" :key="index">
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
                </b-col>
            </b-row>
            <h3 style="margin-top: 30px;">가상피팅할 사진</h3>
        </b-container>
    </div>
</template>

<script>
import ButtonList from './ButtonList'

export default {
    components: {
        ButtonList
    },

    data() {
        return {
            beforeVrfitting: {
                images: [
                    "https://shopping-phinf.pstatic.net/main_1164266/11642661041.20191216104903.jpg?type=f300",
                    "https://shopping-phinf.pstatic.net/main_2955567/29555674431.20221013162919.jpg?type=f300",
                    "https://shopping-phinf.pstatic.net/main_2955567/29555674431.20221013162919.jpg?type=f300",
                ],
            },
            selectedImageIndex: null,
        };
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

        deleteImage(index) {
                this.beforeVrfitting.images.splice(index, 1);
        },
        isRepresentativeImage(index) {
            return index === 0;
        },
        handleImageUpload(event) {
            const file = event.target.files[0];

            if (file) {
                const reader = new FileReader();
                reader.onload = () => {
                    if (!this.beforeVrfitting.images) {
                        this.$set(this.beforeVrfitting, "images", []);
                    }
                    if (this.selectedImageIndex !== null) {
                        this.beforeVrfitting.images[this.selectedImageIndex] = reader.result;
                        this.selectedImageIndex = null;
                    } else {
                        this.afterVrfitting.images.push(reader.result);
                    }
                    this.$forceUpdate();
                };
                reader.readAsDataURL(file);
            }
        },
    },
};
</script>

<style scoped></style>
