
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
      formData.append('dirname', localStorage.getItem("dirname"));
      axios.post("/vf/humanimg", formData)
          .then(response => {
            console.log("실행 시작")
            if(response.status == 200){
              console.log("접속 성공")
              this.composedImageUrl = response.data.resultSrc;
              alert('이미지가 합성되었습니다!');
            } else if(response.status == 204){
              alert('로그인을 해야 가상피팅을 진행할 수 있습니다.');
            }
          })
          .catch(error => {
            console.error(error);
            alert('이미지 합성에 실패했습니다!');
          });
    },
    saveImage() {
      const formData = new FormData();
      formData.append('dirname', localStorage.getItem("dirname"));
      formData.append('prodId', this.item_id);

      axios.post('/vf/savestyle', formData)
          .then(response => {
            if(response.status == 200){
              alert('이미지가 저장되었습니다!');
            } else if(response.status == 204){
              alert('로그인을 해야 가상피팅 결과를 저장할 수 있습니다.');
            }
          })
          .catch(error => {
            console.error(error);
            alert('이미지 저장에 실패했습니다!');
          });
    },
  }
};
</script>
