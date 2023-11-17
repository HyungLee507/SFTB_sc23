<template>
    <div class="cartpage">
        <b-container>
            <br>
            <b-row>
                <b-col md="12">
                    <h1>장바구니</h1>
                    <br><br>
                </b-col>
            </b-row>
            <b-row>
                <b-col md="3">
                    <button-list />
                </b-col>
                <b-col md="9">
                    <div v-if="cartItems.length > 0">
                        <table>
                            <thead>
                                <tr>
                                    <th>선택</th>
                                    <th>이미지</th>
                                    <th>상품명</th>
                                    <th>가격</th>
                                    <th>삭제</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="(item, index) in cartItems" :key="index">
                                    <td>
                                        <input type="checkbox" v-model="item.selected" />
                                    </td>
                                    <td>
                                        <img :src="item.images[0]" alt="Image" width="50">
                                    </td>
                                    <td>{{ item.name }}</td>
                                    <td>{{ item.price }}</td>
                                    <td>
                                        <button @click="removeItem(index)" class="delete-button">X</button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <p>총 결제금액: {{ totalAmount }} 원</p>
                        <button @click="onCheckout">주문하기</button>
                    </div>
                    <div v-else>
                        <p>장바구니가 비어 있습니다.</p>
                    </div>
                </b-col>
            </b-row>
        </b-container>
    </div>
</template>




<!-- <script>
import axios from 'axios';
export default {
    data() {
        return {
            cartItems: [],
            tableFields: [
                { key: 'selected', label: '선택' },
                { key: 'images', label: '이미지' },
                { key: 'name', label: '상품명' },
                { key: 'price', label: '가격' }
            ]
        };
    },
    computed: {
        totalAmount: function () {
            return this.cartItems.reduce(function (total, item) {
                return item.selected ? total + item.price : total;
            }, 0);
        }
    },

    created() {
        this.getCartItems();
    },
    methods: {
        getCartItems() {
            axios
                .get('/cart')
                .then((response) => {   //백엔드에서 어떻개 받지
                    this.cartItems = response.data.map(item => ({
                        images: item.images,
                        name: item.name,
                        price: item.price,
                        showSize: item.showSize,
                        category: item.category,
                        description: item.description
                    }));
                })
                .catch((error) => {
                    console.error(error);
                });
        },
        onCheckout() {           // 결제
            
        },
        removeItem(index) {
            this.cartItems.splice(index, 1);
            // 서버에 저장된 목록 제거 로직 구현해야함 
        },
    },
};
</script>

//테스트용// -->

<script>
import ButtonList from './ButtonList'

export default {

    components: {
        ButtonList
    },
    data() {
        return {
            cartItems: [
                {
                    selected: false, 
                    images: ['data:image/jpeg;base64,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'],
                    name: '나이키',
                    price: 10000
                },
                {
                    selected: false,  
                    images: ['data:image/jpeg;base64,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'],
                    name: '아디다스',
                    price: 15000
                }
            ],
            tableFields: [
                { key: 'selected', label: '선택' },
                { key: 'images', label: '이미지' },
                { key: 'name', label: '상품명' },
                { key: 'price', label: '가격' }
            ]
        };
    },
    computed: {
        totalAmount: function () {
            return this.cartItems.reduce(function (total, item) {
                return item.selected ? total + item.price : total;
            }, 0);
        }
    },
     methods: {                //결제
        onCheckout() {
            
        },
        removeItem(index) {
            this.cartItems.splice(index, 1);
            // 서버에 저장된 목록 제거 로직 구현해야함
        },
    }
};
</script>

<style>
.container {
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
}

table {
    border-collapse: collapse;
    width: 100%;
}

th,
td {
    text-align: left;
    padding: 10px;
    border-bottom: 1px solid #ddd;
}

th {
    background-color: #f2f2f2;
}

img {
    width: 50px;
    height: 50px;
    border-radius: 50%;
}

button {
    background-color: #4CAF50;
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 16px;
}

.delete-button {
    background-color: transparent;
    color: #000000;
    border: none;
    font-size: 16px;
    cursor: pointer;
}
</style>