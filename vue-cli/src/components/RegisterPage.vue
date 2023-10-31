<template>
    <div class="container d-flex justify-content-center">
    <b-form  @submit.stop.prevent>
        <label for="text-email">Email</label>
        <b-form-input v-model="form.Email"  type="email" id="text-email" aria-describedby="email-help-block" required></b-form-input>
    
        <label for="text-password">Password</label>
        <b-form-input v-model="form.Password"  type="password" id="text-password" aria-describedby="password-help-block" required></b-form-input>
        
        
        <label for="text-nickname">NickName</label>
        <b-form-input v-model="form.NickName" type="nickname" id="text-nickname" aria-describedby="nickname-help-block" required></b-form-input>
        <b-button type="submit" variant="primary">회원가입</b-button>
        </b-form>
        
    </div>

    </template>

    <script>
    export default {
    data() {
        return {
            form: {
                UserID: '',
                Password: '',
                NickName: ''
            }
        }
    }
    }
    </script> 