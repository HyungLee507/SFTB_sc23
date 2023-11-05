<template>
    <div class="container d-flex justify-content-center">
    <b-form  @submit="submitForm">
    <div class="form-group">
        <label for="text-email">이메일</label>
        <b-form-input v-model="form.email" type="email" id="text-email" aria-describedby="email-help-block" required></b-form-input>
        <b-button @click="sendVerificationCode" variant="info" style="margin-left: 10px;">인증번호 발송</b-button>
    </div>
    <div class="form-group">
        <label for="text-password">인증번호 입력</label>
            <b-form-input v-model="VerificationCode"  type="number" id="text-verificationcode" aria-describedby="verificationcode-help-block" required></b-form-input>
            <b-button @click="VerificationCheck" variant="info" style="margin-left: 10px;">확인</b-button>
    </div>
    
        <label for="text-password" v-show="isVisable">새 비밀번호</label>
        <b-form-input v-model="form.password" v-show="isVisable"  type="password" id="text-password" aria-describedby="password-help-block" required></b-form-input>
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
                    email: '',
                    password: '',
                },

                VerificationCode:'',//사용자가 입력한 인증번호
                isVisable: false,
                newPassword: '',//서버로 부터 받은 인증번호
            
            };
        },
        methods: {
        
        sendVerificationCode(){
            
            if(this.form.email ==''){
                alert('이메일을 입력하세요')
            }
            else if(!/^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]+$/.test(this.form.email)){
                alert('올바른 이메일 주소를 입력하세요.');
                return;
            }
            else{
                const eform = new FormData();
                eform.append('email',this.form.email);//이메일 저장
                axios.post('http://localhost:8080/find-password',eform)//이메일 인증api 호출 form의 형태로 이메일 전달
                    .then((res) => {
                        // if(res.data.exist){
                        //     alert(res.data.exist)//중복확인
                        // }
                        if(res.status == 200){
                            alert('이메일이 발송되었습니다');//이메일 발송
                            const key = res.data;
                            alert(res.data);
                            console.log(res.data)
                            this.newPassword = key;//api로부터 전달받은 key값저장
                        }
                        else{
                            alert('잘못된 이메일입니다');
                        }
                    })
            }
            

        },

        inputVerificationCode(event){
            this.VerificationCode = event.target.value;
        },
        VerificationCheck(){
            if(this.VerificationCode==''){
                alert('인증번호를 입력하세요');
            }
            else{
                if (this.VerificationCode == this.newPassword) {
                        alert('인증 성공');
                        this.isVisable = true;
                    }
                    else {
                        alert('인증번호가 일치하지 않습니다');
                    }
            }
            
        },

        submitForm() {

                if (!this.form.email || !this.form.password) {
                    alert('모든 항목을 입력하세요.');
                    return;
                }

                if (!/^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]+$/.test(this.form.email)) {
                    alert('올바른 이메일 주소를 입력하세요.');
                    return;
                }

                const eform = new FormData()
                eform.append('email',this.form.email);//이메일 저장
                eform.append('password',this.form.password);
                axios.post('http://localhost:8080/change-password', eform).then((res) => {
                    if(res.status == 200){
                      alert('비밀번호 변경이 완료되었습니다.');
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