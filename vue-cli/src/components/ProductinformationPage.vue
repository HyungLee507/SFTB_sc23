<template>
    <b-container>
        <b-row>
            <b-col>
<<<<<<< HEAD
                <b-carousel id="carousel-1" v-model="slide" :interval="4000" controls indicators background="#ababab"
                    img-width="1024" img-height="480" style="text-shadow: 1px 1px 2px #333;">
                    <b-carousel-slide v-for="(image, index) in product.images" :key="index" :img-src="image"
                        :alt="'Product image ' + (index + 1)"></b-carousel-slide>
=======
                <b-carousel
                    id="carousel-1"
                    v-model="slide"
                    :interval="4000"
                    controls
                    indicators
                    background="#ababab"
                    img-width="1024"
                    img-height="480"
                    style="text-shadow: 1px 1px 2px #333;"
                >
                    <b-carousel-slide
                        v-for="(image, index) in product.images"
                        :key="index"
                        :img-src="image"
                        :alt="'Product image ' + (index + 1)"
                    ></b-carousel-slide>
>>>>>>> origin/social_login
                </b-carousel>
            </b-col>
        </b-row>

        <b-row>
            <b-col>
                <h1>{{ product.name }}</h1>
                <p>{{ product.price }}</p>
                <p>{{ product.description }}</p>
                <b-button variant="primary" to="#">장바구니</b-button>
                <b-button variant="success" to="#">VR Fitting</b-button>
                <b-button variant="info" to="#">결제</b-button>
            </b-col>
        </b-row>
    </b-container>
</template>

<script>
import axios from "axios";

export default {
    data() {
        return {
            product: {//상품정보를 저장할 객체
                images: [],
                name: "",
                price: 0,
                description: "",
            },
            slide: 0,
        };
    },
    created() {
        const id = this.$route.params.id;//상품의 id를 받아옴
        axios
            .get("/api/products/list/" + id) //상품정보를 받아오는 api호출
            .then((response) => {
                this.product = response.data;//상품정보를 받아옴
            })
            .catch((error) => {
                console.log(error);
            });
    },
};
</script>