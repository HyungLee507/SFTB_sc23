<template>
  <div class="ordersgpage">
    <b-container>
      <b-row>
        <b-col md="12">
          <h1 style="margin-bottom: 40px;">주문내역</h1>
        </b-col>
      </b-row>
      <b-row>
        <b-col md="3">
          <button-list/>
        </b-col>
        <b-col md="9">
          <div v-if="ordersProducts.length > 0">
            <table>
              <thead>
              <tr>
                <th>이미지</th>
                <th>상품명</th>
                <th>가격</th>
                <th>배송상태</th>
                <th>주문취소</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(item, index) in ordersProducts" :key="index">
                <td>
                  <img :src="item.images[0]" alt="Image" width="50">
                </td>
                <td>{{ item.name }}</td>
                <td>{{ item.price }}</td>
                <td>{{ item.status }}</td>
                <td>
                  <button v-if="item.status === '배송중'" @click="removeItem(index)" class="delete-button">X</button>
                  <span v-else>취소불가</span>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
          <div v-else>
            <p>구매한 상품이 없습니다.</p>
          </div>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<!-- <script>
import ButtonList from './ButtonList';
import axios from 'axios';

export default {
    components: {
        ButtonList,
    },
    data() {
        return {
            ordersProducts: [],
        };
    },

    created() {
        this.getOrdersProducts();
    },

    methods: {
        getOrdersProducts() {
            axios
                .get('/order')
                .then((response) => {
                    this.ordersProducts = response.data.map((item) => ({
                        images: item.images,
                        name: item.name,
                        price: item.price,
                        shoeSize: item.shoeSize
                        category: item.category
                        description: item.description,
                        status: item.status,
                    }));
                })
                .catch((error) => {
                    console.error(error);
                });
        },
        removeItem(index) {
            this.ordersProducts.splice(index, 1);
            // 서버에 저장된 목록 제거 로직 구현해야 함 ( 배송 취소 ? )
        },
    },
};
</script> -->

<script>
import ButtonList from './ButtonList';

export default {
  components: {
    ButtonList,
  },
  data() {
    return {
      ordersProducts: [
        {
          images: ['https://example.com/image1.jpg'],
          name: '상품1',
          price: 10000,
          status: '배송중',
        },
        {
          images: ['https://example.com/image2.jpg'],
          name: '상품2',
          price: 15000,
          status: '배송완료'
        },
      ],
    };
  },
  methods: {
    removeItem(index) {
      this.ordersProducts.splice(index, 1);
      // 서버에 저장된 목록 제거 로직 구현해야 함  ( 배송 취소 ? )
    },
  },
};
</script>


<style scoped>

</style>