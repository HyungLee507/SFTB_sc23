<template>
  <div class="container d-flex justify-content-center">
    <b-form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="text-email" class="info">이메일</label>
        <b-form-input class="input" v-model="form.email" type="email" id="text-email" aria-describedby="email-help-block"
                      required></b-form-input>
        <b-button @click="sendVerificationCode" class="info-button">인증번호 발송</b-button>
      </div>
      <div class="form-group">
        <label for="text-password" class="info">인증번호 입력</label>
        <b-form-input class="input" v-model="form.verificationCode" type="number" id="text-verificationcode"
                      aria-describedby="verificationcode-help-block" required></b-form-input>
        <b-button @click="VerificationCheck" class="info-button" style="margin-left: 10px;">확인</b-button>
      </div>

      <label for="text-password" class="info">비밀번호</label>
      <b-form-input class="input" v-model="form.password" type="password" id="text-password" aria-describedby="password-help-block"
                    required></b-form-input>
      <div class="form-group">
        <label for="password-confirm">비밀번호 확인</label>
        <b-form-input class="input" type="password" id="password-confirm" v-model="passwordConfirm" required></b-form-input>
        <p v-if="passwordsMatch && form.password && passwordConfirm" style="color: green;">비밀번호가 같습니다.</p>
        <p v-else-if="form.password && passwordConfirm" style="color: red;">비밀번호가 다릅니다.</p>
      </div>
      <label for="text-name" class="info">이름</label>
      <b-form-input class="input" v-model="form.name" type="text" id="text-name" aria-describedby="name-help-block"
                    required></b-form-input>
      <b-form-group id="product-shoeSize" label="선호 사이즈" class="info">
            <b-form-radio-group v-model="form.footSize">
                <b-form-radio value="85">85</b-form-radio>
                <b-form-radio value="90">90</b-form-radio>
                <b-form-radio value="95">95</b-form-radio>
                <b-form-radio value="100">100</b-form-radio>
                <b-form-radio value="105">105</b-form-radio>
                <b-form-radio value="110">110</b-form-radio>
            </b-form-radio-group>
          </b-form-group>
      <b-button type="submit" v-show="isVisable" variant="primary">회원가입</b-button>
    </b-form>

  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      form: {
        email: '',
        verificationCode: '',
        password: '',
        name: '',
        footSize: '',
      },

      isVisable: false,
      passwordConfirm: '',

    };
  },
  computed: {
    passwordsMatch() {
      return this.form.password === this.passwordConfirm;
    }
  },
  methods: {

    validatePassword(password) {
      const regex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]).{10,}$/;
      return regex.test(password);
    },

    sendVerificationCode() {
      if (this.isVisable) {
        alert("이미 인증한 번호입니다.");
        return;
      }

      if (this.form.email == '') {
        alert('이메일을 입력하세요')
      } else if (!/^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]+$/.test(this.form.email)) {
        alert('올바른 이메일 주소를 입력하세요.');
        return;
      } else {
        const eform = new FormData();
        eform.append('email', this.form.email);
        axios.post('/mail-verify', eform)
            .then((res) => {
              if (res.status == 200) {
                alert('이메일이 발송되었습니다');
              } else if (res.status == 226) {
                alert('이미 인증번호를 발송한 이메일입니다.');
              } else {
                alert('잘못된 이메일입니다');
              }
            }).catch((err) => {
          console.log(err)
        })
      }


    },
    checkPassword() {
      if (this.form.password !== this.passwordConfirm) {
        alert('비밀번호가 다릅니다.');
      }
    },


    VerificationCheck() {
      if (this.form.verificationCode == '') {
        alert('인증번호를 입력하세요');
      } else {
        const eform = new FormData();
        eform.append('email', this.form.email);
        eform.append('verificationCode', this.form.verificationCode);
        axios.post('/check-verification', eform)
            .then((res) => {
              if (res.status == 200) {
                alert('인증에 성공했습니다.');
                this.isVisable = true;
              } else if (res.status == 204) {
                alert('이메일을 다시 확인해주십시오.');
              }
            }).catch((err) => {
          if (err.response.status == 401) {
            alert('인증번호가 틀렸습니다.');
          } else if(err.response.status == 408){
            alert("인증 기간이 만료되었습니다.")
          } else {
            alert('잘못된 요청입니다.')
          }
        });
      }

    },

    async submitForm() {

      if (!this.form.email || !this.form.password) {
        alert('모든 항목을 입력하세요.');
        return;
      }
      if (!this.validatePassword(this.form.password)) {
        alert('비밀번호는 영문, 숫자, 특수문자 중 2종류 이상을 조합하여 최소 10자리 이상 또는 3종류 이상을 조합하여 최소 8자리 이상의 길이로 구성해야 합니다.');
        return;
      }

      if (!/^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]+$/.test(this.form.email)) {
        alert('올바른 이메일 주소를 입력하세요.');
        return;
      }
      if (!this.passwordsMatch) {
        alert('비밀번호와 비밀번호 확인이 일치하지 않습니다.');
        return;
      }


      await axios.post('/sign-up', this.form).then((res) => {
        if (res.status == 200) {
          alert('회원가입이 완료되었습니다.');
          this.$router.push('/account/login');
        }
      }).catch((err) => {
        alert('회원가입에 실패했습니다. 다시 시도해주세요.');
        console.log(err);
      });
    },
  }
};
</script>


<style scoped>
.container {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: bold;
  font-size: 20px;
  margin-bottom: 200px;
}

.info-button {
  margin-top: 10px;
  background-color: rgb(39, 161, 39);
  font-weight: bolder;
}

.info {
  margin-top: 35px;
}

.input {
  font-weight: bold;
}
</style>


