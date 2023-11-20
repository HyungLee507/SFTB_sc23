<template>
  <div>
    <h1>Payment Page</h1>
    <form @submit.prevent="submitForm">
      <label for="address">배송 주소:</label>
      <input type="text" id="address" v-model="address" required>
      <br>
      <label for="name">수령인:</label>
      <input type="text" id="name" v-model="name" required>
      <br>
      <label for="contact">전화 번호:</label>
      <input type="text" id="contact" v-model="contact" required>
      <label for="postcode">우편 번호:</label>
      <input type="text" id="postcode" v-model="postcode" required>
      <br>
      <button type="submit" @click="PaymentBtn">Submit</button>
    </form>
  </div>
</template>
<script src="https://cdn.iamport.kr/v1/iamport.js"></script>
<script>
//import axios from "axios";
export default {
  data() {
    return {
      address: "",
      name: "",//수령인
      contact: "",//연락처
      postcode: "",
      id: this.$route.params.id,
      userName: "",
      userEmail: "",
      productName: "",
      productPrice: 0
    };
  },
  methods: {
    // submitForm() {
    //     const formData = {
    //         address: this.address,
    //         name: this.name,
    //         contact: this.contact,
    //         postcode: this.postcode,
    //         id: this.id
    //     };

    //     axios.post("/api/submit-form", formData)
    //         .then(response => {
    //             console.log(response.data);
    //         })
    //         .catch(error => {
    //             console.error(error);
    //         });
    // },
    PaymentBtn: function () {

      IMP.init("imp37227376");

      IMP.request_pay({ // param
        // pg: "html5_inicis",
        // pay_method: "card",
        // merchant_uid: this.id,
        // name: this.userName,
        // amount: this.productPrice,
        // buyer_email: this.userEmail,
        // buyer_name: this.userName,
        // buyer_tel: this.contact,
        // buyer_addr: this.address,
        // buyer_postcode: this.postcode,
        pg: "html5_inicis",
        pay_method: "card",
        merchant_uid: "1",
        name: "전강우",
        amount: "1",
        buyer_email: "kwjeon98@gmail.com",
        buyer_name: "전강우",
        buyer_tel: "010-1234-5678",
        buyer_addr: "서울특별시 강남구 신사동 661-16",
        buyer_postcode: "06018",
      }, rsp => { // callback
        console.log(rsp);
        if (rsp.success) {
          console.log("결제 성공");
        } else {
          console.log("결제 실패");
        }
      });

    },
    // fetchData() {
    //     axios.get("/api/user-info")
    //         .then(response => {
    //             this.userName = response.data.userName;
    //             this.userEmail = response.data.userEmail;
    //             this.productName = response.data.productName;
    //             this.productPrice = response.data.productPrice;
    //         })
    //         .catch(error => {
    //             console.error(error);
    //         });
    // }
  },
  // mounted() {
  //     this.fetchData();
  // },
};

</script>