<template>
  <div class="container d-flex justify-content-center">
    <b-form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="text-name" style="margin-top: 30px;">이름</label>
        <b-form-input v-model="form.name" type="text" id="text-name" :readonly="isEmailSent" required></b-form-input>
      </div>
      <div class="form-group">
        <label for="text-email" style="margin-top: 30px;">이메일</label>
        <b-form-input v-model="form.email" type="email" id="text-email" aria-describedby="email-help-block"
                      :readonly="isEmailSent" required></b-form-input>
        <b-button @click="sendVerificationCode" variant="info" class="buttons">인증번호 발송</b-button>
      </div>
      <div class="form-group">
        <label for="text-password">인증번호 입력</label>
        <b-form-input v-model="form.verificationCode" type="number" id="text-verificationcode"
                      aria-describedby="verificationcode-help-block" required></b-form-input>
        <b-button @click="VerificationCheck" variant="info" class="buttons">확인</b-button>
      </div>

      <label for="text-password" v-show="isVisable">새 비밀번호</label>
      <b-form-input v-model="form.password" v-show="isVisable" type="password" id="text-password"
                    aria-describedby="password-help-block" required></b-form-input>
      <div class="form-group" v-show="isVisable">
        <label for="password-confirm">새 비밀번호 확인</label>
        <b-form-input class="input" type="password" id="password-confirm" v-model="passwordConfirm"
                      required></b-form-input>
        <p v-if="passwordsMatch && form.password && passwordConfirm" style="color: green;">비밀번호가 같습니다.</p>
        <p v-else-if="form.password && passwordConfirm" style="color: red;">비밀번호가 다릅니다.</p>
      </div>
      <b-button type="submit" v-show="isVisable" variant="primary">비밀번호 변경</b-button>
    </b-form>

  </div>

</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      form: {
        name: '',
        email: '',
        password: '',
        verificationCode: '',
      },
      isEmailSent: false,
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
    sendVerificationCode() {
      if (this.form.name == '') {
        alert('이름을 입력하세요')
      } else if (this.form.email == '') {
        alert('이메일을 입력하세요')
      } else if (!/^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]+$/.test(this.form.email)) {
        alert('올바른 이메일 주소를 입력하세요.');
        return;
      } else {
        const eform = new FormData();
        eform.append('name', this.form.name);
        eform.append('email', this.form.email);
        axios.post('/find-password', eform)
            .then((res) => {
              if (res.status == 200) {
                alert('인증번호가 이메일로 발송되었습니다\n3분 내로 입력 바랍니다');
                this.isEmailSent = true;
              } else if (res.status == 226) {
                alert('이미 인증번호를 발송한 이메일입니다.');
              } else if (res.status == 204) {
                alert('존재하지 않는 이메일입니다.');
              }
            }).catch((err) => {
          if (err.response.status == 401) {
            alert('일치하는 정보가 없습니다.');
          } else {
            alert('잘못된 요청입니다.')
          }
        })
      }
    },
    // inputVerificationCode(event) {
    //   this.verificationCode = event.target.value;
    // },
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
          } else if (err.response.status == 408) {
            alert("인증 기간이 만료되었습니다.")
          } else {
            alert('잘못된 요청입니다.')
          }
        });
      }

    },
    submitForm() {

      if (!this.form.email || !this.form.password || !this.form.name) {
        alert('모든 항목을 입력하세요.');
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

      const eform = new FormData()
      eform.append('email', this.form.email);
      eform.append('password', this.form.password);
      axios.post('/change-password', eform).then((res) => {
        if (res.status == 200) {
          alert('비밀번호 변경이 완료되었습니다.');
          this.$router.push('/account/login');
        } else {
          alert('비밀번호 변경에 실패했습니다. 다시 시도해주세요.');
        }
      }).catch(() => {
        alert('비밀번호 변경에 실패했습니다. 다시 시도해주세요.');
      });
    },
  }
};
</script>

<style scoped>
.container {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: bold;
  margin-bottom: 200px;
}

.buttons {
  margin-top: 10px;
  margin-bottom: 30px;
  background-color: rgba(0, 0, 0, 0.753);
  border: 0px;
}
</style>