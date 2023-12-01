<template>
  <div>
    <b-form-group label=" 수령인 이름">
      <b-form-input v-model="receiver_name"></b-form-input>
    </b-form-group>
    <b-form-group label="전화번호">
      <b-form-input v-model="buyer_tel"></b-form-input>
    </b-form-group>
        <b-form-group label="우편번호">
        <b-form-input v-model="buyer_postcode"></b-form-input>
        <input type="button" @click="execDaumPostcode()" value="우편번호 찾기"><br>

      </b-form-group>
    <b-form-group label="주소">
      <b-form-input v-model="buyer_addr"></b-form-input>
    </b-form-group>
    <b-form-group label="상세주소">
      <b-form-input v-model="buyer_addr_detail"></b-form-input>
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
    execDaumPostcode() {
      new window.daum.Postcode({
        oncomplete: (data) => {
          if (this.buyer_addr_detail !== "") {
            this.buyer_addr_detail = "";
          }
          if (data.userSelectedType === "R") {
            // 사용자가 도로명 주소를 선택했을 경우
            this.buyer_addr = data.roadAddress;
          } else {
            // 사용자가 지번 주소를 선택했을 경우(J)
            this.buyer_addr = data.jibunAddress;
          }
 
          // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
          if (data.userSelectedType === "R") {
            // 법정동명이 있을 경우 추가한다. (법정리는 제외)
            // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
            if (data.bname !== "" && /[동|로|가]$/g.test(data.bname)) {
              this.buyer_addr_detail += data.bname;
            }
            // 건물명이 있고, 공동주택일 경우 추가한다.
            if (data.buildingName !== "" && data.apartment === "Y") {
              this.buyer_addr_detail +=
                this.buyer_addr_detail !== ""
                  ? `, ${data.buildingName}`
                  : data.buildingName;
            }
            // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
            if (this.buyer_addr_detail !== "") {
              this.buyer_addr_detail = `(${this.buyer_addr_detail})`;
            }
          } else {
            this.buyer_addr_detail = "";
          }
          // 우편번호를 입력한다.
          this.buyer_postcode = data.zonecode;
        },
      }).open();
    },
    KGpay() {
      axios.get('/product-detail/' + this.item_id)
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
              buyer_addr: this.buyer_addr + this.buyer_addr_detail,
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