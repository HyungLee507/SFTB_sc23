<template>
    <div class="mypage">
        <b-container>
            <br>
            <b-row>
                <b-col md="12">
                    <h1>마이페이지</h1>
                    <br><br>
                </b-col>
            </b-row>
            <b-row>
                <b-col md="3">
                    <button-list />
                </b-col>
                <b-col md="9">
                    <h2>내용내용 </h2>
                </b-col>
            </b-row>
        </b-container>
    </div>
</template>

<style scoped>


</style>

<script>

import ButtonList from './ButtonList'

export default {
    components: {
        ButtonList
    },
};


</script>
