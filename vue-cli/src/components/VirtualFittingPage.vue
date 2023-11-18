<template>
  <div class="virtualfittingpage">
    <b-container>
      <br>
      <b-row>
        <b-col md="12">
          <h1>가상 피팅</h1>
          <br><br>
        </b-col>
      </b-row>
      <b-row>
        <b-col md="3">
          <button-list/>
        </b-col>
        <b-col md="9">
          내용내용
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
import ButtonList from './ButtonList'

export default {
  components: {
    ButtonList
  },
};
</script>

<style scoped></style>