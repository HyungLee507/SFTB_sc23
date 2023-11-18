<template>
    <div class="virtualfittingpage">
        <b-container>
            <b-row>
                <b-col md="12">
                    <h1 style="margin-bottom: 40px;">가상 피팅</h1>
                </b-col>
            </b-row>
            <b-row>
                <b-col md="3">
                    <button-list />
                </b-col>
                <b-col md="9">
                    <h3>가상피팅된 사진</h3>
                    
                </b-col>
            </b-row>
            <h3 style="margin-top: 30px;">가상피팅할 사진</h3>
        </b-container>
    </div>
</template>

<script>
import ButtonList from './ButtonList'

export default {
    components: {
        ButtonList
    },

    data() {
        return {
            afterVrfitting: {
            
            }
        }
    }
};
</script>

<style scoped></style>
