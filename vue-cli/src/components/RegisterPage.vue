<template>
    <div class="container d-flex justify-content-center">
    <b-form  @submit="submitForm">
    <div class="form-group">
        <label for="text-email">이메일</label>
        <b-form-input v-model="form.Email" type="email" id="text-email" aria-describedby="email-help-block" required></b-form-input>
        <b-button @click="sendVerificationCode" variant="info" style="margin-left: 10px;">인증번호 발송</b-button>
    </div>
        <label for="text-password">인증번호 입력</label>
            <b-form-input v-model="form.Password"  type="verificationcode" id="text-verificationcode" aria-describedby="verificationcode-help-block" required></b-form-input>

    
        <label for="text-password">비밀번호</label>
        <b-form-input v-model="form.Password"  type="password" id="text-password" aria-describedby="password-help-block" required></b-form-input>
        
        
        <label for="text-nickname">닉네임</label>
        <b-form-input v-model="form.NickName"  type="text" id="text-nickname" aria-describedby="nickname-help-block" required></b-form-input>

        <label for="text-birthday">생일</label>
        <b-form-input v-model="form.BirthDay" type="text" id="text-birthday" aria-describedby="birthday-help-block" required></b-form-input>
        <b-form-text id="birthday-help-block">
        생일은 YYYY-MM-DD 형식으로 입력해 주세요
        </b-form-text>
        <label for="text-birthday">발 사이즈</label>
        <b-form-input v-model="form.Footsize" type="number" id="text-footsize" aria-describedby="footsize-help-block" required></b-form-input>
        <b-button type="submit" variant="primary">회원가입</b-button>
        </b-form>
        
    </div>

    </template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            form: {
                Email: '',
                Password: '',
                NickName: '',
                BirthDay: '',
                Footsize: '',
            },

            VerificationCode:'',
        
        };
    },
    methods: {
    
    sendVerificationCode(){

    },
    submitForm() {

            if (!this.form.Email || !this.form.Password || !this.form.NickName || !this.form.BirthDay) {
                alert('모든 항목을 입력하세요.');
                return;
            }

            if (!/^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]+$/.test(this.form.Email)) {
                alert('올바른 이메일 주소를 입력하세요.');
                return;
            }

            
            if (!/^\d{4}\d{2}\d{2}$/.test(this.form.BirthDay)) {
                alert('올바른 생일을 입력하세요.');
                return;
            }
            if (!/^\d{3}$/.test(this.form.Footsize)) {
                alert('올바른 발사이즈를 입력하세요');
                return;
            }

            
            axios.post('/register', this.form).then(() => {
                alert('회원가입이 완료되었습니다. 이메일 인증 링크를 확인해주세요.');
            }).catch(() => {
                alert('회원가입에 실패했습니다. 다시 시도해주세요.');
            });
        },
    }
};
</script>