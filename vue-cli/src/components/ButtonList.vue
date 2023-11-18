<template>
  <b-list-group>
    <b-list-group-item v-for="(button, index) in buttons" :key="index" @click="button.action"
                       @mouseover="highlightButton(button)" @mouseout="unhighlightButton(button)"
                       :style="{ backgroundColor: button.backgroundColor, cursor: button.cursor }">
      {{ button.label }}
    </b-list-group-item>
  </b-list-group>
</template>


<style>
.container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0px;
  /* 벽에 붙게 만들어야함 */
}
</style>

<script>
export default {
  props: {
    button: Array
  },
  data() {
    return {
      buttons: [
        {
          label: "주문내역",
          action: this.goToMyPage,
          backgroundColor: "transparent",
        },
        {
          label: "장바구니",
          action: this.goToCartPage,
          backgroundColor: "transparent",

        },
        {
          label: "내 정보",
          action: this.goToProfilePage,
          backgroundColor: "transparent",

        },
        {
          label: "가상피팅",
          action: this.goToVirtualFittingPage,
          backgroundColor: "transparent",
        },
        {
          label: "판매내역",
          action: this.goToSalesHistoryPage,
          backgroundColor: "transparent",
        },
      ],
    };
  },
  methods: {
    highlightButton(button) {
      button.backgroundColor = "#007bff";
      button.cursor = "pointer";
    },
    unhighlightButton(button) {
      button.backgroundColor = "transparent";
      button.cursor = "default";
    },

    goToMyPage() {
      const targetRoute = "/user/mypage";
      this.$router.push(targetRoute).catch(() => {
      });
    },
    goToCartPage() {
      const targetRoute = "/user/cart";
      this.$router.push(targetRoute).catch(() => {
      });
    },
    goToProfilePage() {
      const targetRoute = "/user/mypage/profile";
      this.$router.push(targetRoute).catch(() => {
      });
    },
    goToVirtualFittingPage() {
      const targetRoute = "/user/mypage/virtualfitting";
      this.$router.push(targetRoute).catch(() => {
      });
    },
    goToSalesHistoryPage() {
      const targetRoute = "/user/mypage/saleshistory";
      this.$router.push(targetRoute).catch(() => {
      });
    },
  },
};
</script>