<template>
  <div class="paymentpage text-center">
    <b-form-group label=" 수령인 이름" style="margin-top: 40px;">
      <b-form-input style="width: 200px; font-weight: bold;" v-model="receiver_name" class="mx-auto"></b-form-input>
    </b-form-group>
    <b-form-group label="전화번호" style="margin-top: 30px;">
      <b-form-input style="width: 300px; font-weight: bold;" v-model="buyer_tel" class="mx-auto"></b-form-input>
    </b-form-group>
        <b-form-group label="우편번호" style="margin-top: 30px;">
        <b-form-input style="width: 200px; font-weight: bold;" v-model="buyer_postcode" class="mx-auto"></b-form-input>
        <input type="button" @click="execDaumPostcode()" value="우편번호 찾기" style="margin-top: 10px;">

      </b-form-group>
    <b-form-group label="주소" style="margin-top: 30px;">
      <b-form-input style="width: 500px; font-weight: bold;" v-model="buyer_addr" class="mx-auto"></b-form-input>
    </b-form-group>
    <b-form-group label="상세주소" style="margin-top: 30px;">
      <b-form-input style="width: 500px; font-weight: bold;" v-model="buyer_addr_detail" class="mx-auto"></b-form-input>
    </b-form-group>
    
    <b-button @click="KGpay" variant="info" class="pay-button">결제</b-button>
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
      receiver_name: '',
      buyer_tel: '',
      buyer_addr: '',
      buyer_addr_detail: '',
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
      const token = localStorage.getItem('accessToken');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    }, function (error) {
      return Promise.reject(error);
    });
        axios.interceptors.response.use((config) => {
      return config;
    }, function (error) {
      if (error.response && error.response.status === 401) {
        alert('로그인이 필요합니다.');
        this.$router.push('/account/login');
      }
      return Promise.reject(error);
    });
  },
  methods: {
    execDaumPostcode() {
      new window.daum.Postcode({
        oncomplete: (data) => {
          if (this.buyer_addr_detail !== "") {
            this.buyer_addr_detail = "";
          }
          if (data.userSelectedType === "R") {
            this.buyer_addr = data.roadAddress;
          } else {
            this.buyer_addr = data.jibunAddress;
          }

          if (data.userSelectedType === "R") {
            if (data.bname !== "" && /[동|로|가]$/g.test(data.bname)) {
              this.buyer_addr_detail += data.bname;
            }
            if (data.buildingName !== "" && data.apartment === "Y") {
              this.buyer_addr_detail +=
                  this.buyer_addr_detail !== ""
                      ? `, ${data.buildingName}`
                      : data.buildingName;
            }


            if (this.buyer_addr_detail !== "") {
              this.buyer_addr_detail = `(${this.buyer_addr_detail})`;
            }
          } else {
            this.buyer_addr_detail = "";
          }

          this.buyer_postcode = data.zonecode;
        },
      }).open();
    },
    KGpay() {
      axios.get('/product-detail/' + this.item_id)
          .then(response => {

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
              buyer_addr: this.buyer_addr + this.buyer_addr_detail,
              buyer_postcode: this.buyer_postcode,
            }, (response) => {
              if (response.success) {
                this.response = response;
                this.seller_name
                axios
                    .post(`/payment/validation/${response.imp_uid}`, {})
                    .then((res) => {
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
                            this.$router.push('/user/mypage/buyertrading');
                          })
                          .catch((error) => {
                            console.error(error);
                            alert('결제실패 : ' + response.error_msg);
                            this.$router.push('/');
                          });
                    })
                    .catch((error) => {
                      console.error(error);
                      alert('결제실패 : ' + response.error_msg);
                      this.$router.push('/');
                    });
              } else {
                alert('결제실패 : ' + response.error_msg);
                this.$router.push('/');
              }
            });
          })
          .catch(error => {
            console.error(error);
            alert('결제실패 : ' + response.error_msg);
            this.$router.push('/');
          });
    },
  },
};
</script>

<style scoped>

.paymentpage {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: bold;
  font-size: 20px;
  margin-bottom: 200px;
}

.pay-button {
  margin-bottom: 40px;
  margin-top: 30px;
}
</style>