
<template>
  <div>
    <input type="file" @change="handleFileUpload" />
    <img :src="imageUrl" alt="Uploaded Image" v-if="imageUrl" style="max-width: 100%; max-height: 100%;" />
    <button class="upload-button" @click="composeImages">이미지 합성</button>
    <img :src="composedImageUrl" alt="Composed Image" v-if="composedImageUrl" style="max-width: 100%; max-height: 100%;" />
    <button class="save-button" @click="saveImage">이미지 저장</button>
  </div>
</template>

<style scoped>
.upload-button {
  background-color: #4CAF50;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.save-button {
  background-color: #008CBA;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>

<script>
import axios from 'axios';
export default {
  data() {
    return {
      imageUrl: '',// 하드코딩된 이미지 URL
      composedImageUrl: '', // 하드코딩된 합성 이미지 URL
      item_id: this.$route.params.id,
      file: null, // 추가된 file 데이터

    };
  },
  methods: {
    handleFileUpload(event) {
      this.file = event.target.files[0]; // file 데이터 업데이트
      const reader = new FileReader();

      reader.onload = () => {
        this.imageUrl = reader.result;
      };

      reader.readAsDataURL(this.file);
    },
    composeImages() {
      const formData = new FormData();
      formData.append('image', this.file);
      formData.append('item_id', this.item_id);
      formData.append('dirname', localStorage.getItem("dirname"));
      const ngrokHost = localStorage.getItem("hostUrl")
      axios.post(ngrokHost+'showimage', formData)
          .then(response => {
            this.composedImageUrl = response.data.resultSrc;
            alert('이미지가 합성되었습니다!');
          })
          .catch(error => {
            console.error(error);
            alert('이미지 합성에 실패했습니다!');
          });
    },
    saveImage() {
      const formData = new FormData();
      formData.append('image', this.composedImageUrl);

      axios.post('/api/save', formData)
          .then(response => {
            console.log(response.data);
            console.log('Image saved successfully!');
            alert('이미지가 저장되었습니다!');
          })
          .catch(error => {
            console.error(error);
            alert('이미지 저장에 실패했습니다!');
          });
    },
  }
};
</script>
