<template>
  <div>
    <b-form-group label=" 수령인 이름">
      <b-form-input v-model="receiver_name"></b-form-input>
    </b-form-group>
    <b-form-group label="전화번호">
      <b-form-input v-model="buyer_tel"></b-form-input>
    </b-form-group>
    <b-form-group label="주소">
      <b-form-input v-model="buyer_addr"></b-form-input>
    </b-form-group>
    <b-form-group label="우편번호">
      <b-form-input v-model="buyer_postcode"></b-form-input>
    </b-form-group>
    <b-button @click="KGpay">결제</b-button>
  </div>
</template>
<script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.1.5.js"></script>
<script>
import axios from 'axios';

export default {
  mounted() {
    const script = document.createElement('script');
    script.src = 'https://cdn.iamport.kr/js/iamport.payment-1.1.5.js';
    script.onload = () => this.initializeIamport();
    document.head.appendChild(script);
  },
  data() {
    return {
      receiver_name: '', // Declare the receiver_name property
      buyer_tel: '',
      buyer_addr: '',
      buyer_postcode: '',
      buyer_email: '',
      buyer_name: '',
      amount: 0,
      product_name: '',
      seller_name: '',
      item_id: '',
      response: '',

    };
  },
  created() {
    this.item_id = this.$route.params.id;
    axios.interceptors.request.use((config) => {
      // 요청을 보내기 전에 수행할 작업
      const token = localStorage.getItem('accessToken'); // 로컬 스토리지에서 토큰을 가져옵니다.
      if (token) {
        config.headers.Authorization = `Bearer ${token}`; // 토큰이 있으면 헤더에 추가합니다.
      }
      console.log(config.headers.Authorization);
      return config;
    }, function (error) {
      // 요청 에러 처리
      return Promise.reject(error);
    });
  },
  methods: {
    KGpay() {
      axios.get('/product-list/' + this.item_id)
          .then(response => {
            console.log("sjdfkjsdlfsjl");
            let accessToken = localStorage.getItem('accessToken');
            let base64Url = accessToken.split('.')[1];
            let base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            let jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
              return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
            }).join(''));

            let data = JSON.parse(jsonPayload);
            let username = data.userName;
            let useremail = data.email;
            this.buyer_email = useremail;
            this.amount = response.data.price;
            this.product_name = response.data.name;
            this.buyer_name = username;
            this.seller_name = response.data.sellerName;

            IMP.init('imp37227376');
            IMP.request_pay({
              pg: 'html5_inicis',
              pay_method: 'card',
              merchant_uid: Math.floor(Math.random() * 1000000),
              name: this.product_name,
              amount: this.amount,
              buyer_email: this.buyer_email,
              buyer_name: this.buyer_name,
              buyer_tel: this.buyer_tel,
              buyer_addr: this.buyer_addr,
              buyer_postcode: this.buyer_postcode,
            }, (response) => {
              if (response.success) {
                this.response = response;
                this.seller_name
                console.log(response);
                axios
                    .post(`/payment/validation/${response.imp_uid}`, {})
                    .then((res) => {
                      console.log(res);
                      console.log(this.response);
                      console.log(this.response.imp_uid);
                      console.log(this.response.buyer_tel);


                      axios
                          .post(`/payment`, {
                            seller_name: this.seller_name,
                            id: this.item_id,
                            apply_num: this.response.apply_num,
                            buyer_addr: this.response.buyer_addr,
                            buyer_email: this.response.buyer_email,
                            buyer_name: this.response.buyer_name,
                            buyer_postcode: this.response.buyer_postcode,
                            buyer_tel: this.response.buyer_tel,
                            card_name: this.response.card_name,
                            card_number: this.response.card_number,
                            imp_uid: this.response.imp_uid,
                            merchant_uid: this.response.merchant_uid,
                            name: this.response.name,
                            paid_amount: this.response.paid_amount,
                            pg_provider: this.response.pg_provider,
                          })
                          .then((res) => {
                            console.log(res);
                            alert('결제완료');
                          })
                          .catch((error) => {
                            console.error(error);
                          });
                    })
                    .catch((error) => {
                      console.error(error);
                    });
              } else {
                alert('결제실패 : ' + response.error_msg);
              }
            });
          })
          .catch(error => {
            console.error(error);
          });
    },
  },
};
</script>