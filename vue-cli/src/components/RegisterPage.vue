<template>
  <div class="container d-flex justify-content-center">
    <b-form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="text-email">이메일</label>
        <b-form-input v-model="form.email" type="email" id="text-email" aria-describedby="email-help-block"
                      required></b-form-input>
        <b-button @click="sendVerificationCode" variant="info" style="margin-left: 10px;">인증번호 발송</b-button>
      </div>
      <div class="form-group">
        <label for="text-password">인증번호 입력</label>
        <b-form-input v-model="form.verificationCode" type="number" id="text-verificationcode"
                      aria-describedby="verificationcode-help-block" required></b-form-input>
        <b-button @click="VerificationCheck" variant="info" style="margin-left: 10px;">확인</b-button>
      </div>

      <label for="text-password">비밀번호</label>
      <b-form-input v-model="form.password" type="password" id="text-password" aria-describedby="password-help-block"
                    required></b-form-input>


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
      },

      isVisable: false,

    };
  },
  methods: {

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
        eform.append('email', this.form.email);//이메일 저장
        axios.post('http://localhost:8080/mail-verify', eform)//이메일 인증api 호출 form의 형태로 이메일 전달
            .then((res) => {
              if (res.status == 200) {
                alert('이메일이 발송되었습니다');//이메일 발송
              } else if (res.status == 226) {
                alert('이미 인증번호를 발송한 이메일입니다.');//이메일 발송
              } else {
                alert('잘못된 이메일입니다');
              }
            }).catch((err) => {
          console.log(err)
        })
      }


    },

    // inputVerificationCode(event){
    //     this.VerificationCode = event.target.value;
    // },
    VerificationCheck() {
      if (this.form.verificationCode == '') {
        alert('인증번호를 입력하세요');
      } else {
        const eform = new FormData();
        eform.append('email', this.form.email);
        eform.append('verificationCode', this.form.verificationCode);
        axios.post('http://localhost:8080/check-verification', eform)
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

      if (!/^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]+$/.test(this.form.email)) {
        alert('올바른 이메일 주소를 입력하세요.');
        return;
      }


      // axios.post('http://localhost:8080/sign-up', this.form).then(() => {
      //   alert('회원가입이 완료되었습니다.');
      // }).catch(() => {
      //   alert('회원가입에 실패했습니다. 다시 시도해주세요.');
      // });
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