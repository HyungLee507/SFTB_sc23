
<template>
    <div>
        <h1>Payment Page</h1>
        <form @submit.prevent="submitForm">
            <label for="address">Delivery Address:</label>
            <input type="text" id="address" v-model="address" required>
            <br>
            <label for="name">Recipient Name:</label>
            <input type="text" id="name" v-model="name" required>
            <br>
            <label for="contact">Contact Number:</label>
            <input type="text" id="contact" v-model="contact" required>
            <label for="postcode">우편번호:</label>
            <input type="text" id="postcode" v-model="postcode" required>
            <br>
            <button type="submit" @click="PaymentBtn">Submit</button>
        </form>
    </div>
</template>

<script src="https://cdn.iamport.kr/v1/iamport.js">
import axios from "axios";
export default {
    data() {
        return {
            address: "",
            name: "",//수령인
            contact: "",//연락처
            postcode: "",
            userName: "",
            userEmail: "",
            productName: "",
            productPrice: 0
        };
    },
    methods: {
        submitForm() {
            const formData = {
                address: this.address,
                name: this.name,
                contact: this.contact,
                postcode: this.postcode
            };

            axios.post("/api/submit-form", formData)
                .then(response => {
                    console.log(response.data);
                })
                .catch(error => {
                    console.error(error);
                });
        },
        fetchData() {
            axios.get("/api/user-info")
                .then(response => {
                    this.userName = response.data.userName;
                    this.userEmail = response.data.userEmail;
                    this.productName = response.data.productName;
                    this.productPrice = response.data.productPrice;
                })
                .catch(error => {
                    console.error(error);
                });
        }
    },
    mounted() {
        this.fetchData();
    },
    PaymentBtn:function(){

    IMP.init("관리자 콘솔 - 내 정보 - 가맹점 식별코드");

      IMP.request_pay({ // param
        pg: "html5_inicis",
        pay_method: "card",
        merchant_uid: "ORD20180131-0000011",
        name: this.userName,
        amount: this.productPrice,
        buyer_email: this.userEmail,
        buyer_name: this.userName,
        buyer_tel: this.contact,
        buyer_addr: this.address,
        buyer_postcode: this.postcode
      }, rsp => { // callback
        console.log(rsp);
        if (rsp.success) {
        console.log("결제 성공");
        } else {
        console.log("결제 실패");
        }
    });

    }
};

</script>


