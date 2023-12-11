<template>
  <div class="uploadpage">
    <input style="margin-top: 40px; margin-bottom: 40px;" type="file" accept=".jpg" @change="handleFileUpload" />
    <p style="font-weight: bold; font-size: 28px;">대표사진의 예시</p>
    <img class="representative" src="../assets/전신 대표사진.jpg" />
    <p class="guideline" style="margin-top: 40px;">다음과 같이 전신사진을 등록해주세요. (이미지의 크기는 768x1024 혹은 3:4 비율을 권장합니다.)</p>
    <p class="guideline">네트워크 환경에 따라 사진 합성에 최대 2분정도 소요될 수 있습니다.</p>
    <div class="long-line"></div>
    <div>
      <img class="userPreview" :src="imageUrl" alt="Uploaded Image" v-if="imageUrl" />
      <button class="upload-button" @click="composeImages">이미지 합성</button>
    </div>
    <div>
      <img class="composedPreview" :src="composedImageUrl" alt="Composed Image" v-if="composedImageUrl" />
      <button class="save-button" @click="saveImage">이미지 저장</button>
    </div>
    <div v-if="showModal" class="modal">
      <div class="modal-content">
        <p>이미지 합성중입니다...</p>
      </div>
    </div>
  </div>
</template>


<script>
import axios from 'axios';
export default {
  data() {
    return {
      imageUrl: '',
      composedImageUrl: '',
      item_id: this.$route.params.id,
      file: null,
      showModal: false,
    };
  },
  created() {
    axios.interceptors.request.use((config) => {
      const token = localStorage.getItem('accessToken');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      console.log(config.headers.Authorization);
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
    handleFileUpload(event) {
      this.file = event.target.files[0];
      const reader = new FileReader();

      reader.onload = () => {
        this.imageUrl = reader.result;
      };

      reader.readAsDataURL(this.file);
    },
    composeImages() {
      this.showModal = true; // Show the modal

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
            this.showModal = false;
          })
          .catch(error => {
            console.error(error);
            alert('이미지 합성에 실패했습니다!');
            this.showModal = false;
          });
    },
    saveImage() {
      const formData = new FormData();
      formData.append('dirname', localStorage.getItem("dirname"));
      formData.append('prodId', this.item_id);

      axios.post('/vf/savestyle', formData)
        .then(response => {
          if (response.status == 200) {
            alert('이미지가 저장되었습니다!');
          } else if (response.status == 204) {
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

<style scoped>
.upload-button {
  background-color: #4CAF50;
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    margin-left: 30px;
    font-weight: bold;
}

.modal {
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}

.modal-content {
  background-color: white;
  padding: 20px;
}

.save-button {
  background-color: #008CBA;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-left: 30px;
  font-weight: bold;
}
.userPreview {
  width: 240px;
  height: 320px;
  border-radius: 0;
  margin-top: 30px;
  font-weight: bold;
}

.composedPreview {
  width: 240px;
  height: 320px;
  border-radius: 0;
  margin-top: 30px;
  font-weight: bold;
}

.representative{
  width: 240px;
  height: 320px;
  border-radius: 0;
  margin-top: 30px;
  font-weight: bold;
}

.uploadpage {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: bold;
}

.long-line {
  height: 3px;
  background-color: black;
  margin-top: 20px;
  margin-bottom: 40px;
  margin-left: 5%;
  width: 90%;
}


</style>
