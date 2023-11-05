<template>
<div>
    <b-form @submit.prevent="submitForm">
    <b-form-group id="input-group-1" label="이름:" label-for="input-1">
        <b-form-input id="input-1" v-model="name" required></b-form-input>
    </b-form-group>
    <b-form-group id="input-group-2" label="신발 사이즈:" label-for="input-2">
        <b-form-input id="input-2" v-model="shoeSize" required></b-form-input>
    </b-form-group>
    <b-button type="submit" variant="primary">제출</b-button>
    </b-form>
</div>
</template>

<script>
import axios from 'axios';
export default {
data() {
    return {
    name: '',
    shoeSize: ''
    }
},
methods: {
    submitForm() {
    const form = {
        name: this.name,
        shoeSize: this.shoeSize
    }
    
    axios.post('/api/user', form)
    .then(() => {
            this.$router.push('/product/list')
        })
        .catch(error => {
        console.log(error)
        })
    }
}
}
</script>
