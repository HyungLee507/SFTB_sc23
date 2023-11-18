<!--<template>-->
<!--  <div>-->
<!--    <b-navbar toggleable="lg" type="dark" variant="info">-->
<!--      <b-navbar-brand to="/">VFMarket</b-navbar-brand>-->

<!--      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>-->

<!--      <b-collapse id="nav-collapse" is-nav>-->
<!--        <b-navbar-nav>-->
<!--          <b-nav-item v-if="!isTokenPresent" to="/product/registration">상품등록</b-nav-item>-->
<!--          <b-nav-item to="/product/list">상품리스트</b-nav-item>-->
<!--        </b-navbar-nav>-->

<!--        &lt;!&ndash; Right aligned nav items &ndash;&gt;-->
<!--        <b-navbar-nav class="ml-auto">-->
<!--          <b-nav-form>-->
<!--            <b-form-input size="sm" class="mr-sm-2" placeholder="Search"></b-form-input>-->
<!--            <b-button size="sm" class="my-2 my-sm-0" type="submit">Search</b-button>-->
<!--          </b-nav-form>-->

<!--          &lt;!&ndash;            <b-nav-item v-if="!isTokenPresent" to="/account/login">로그인</b-nav-item>&ndash;&gt;-->
<!--          <b-nav-item v-if="!isTokenPresent" to="/account/login">로그인</b-nav-item>-->
<!--          <b-nav-item v-if="!isTokenPresent" to="/account/join">회원가입</b-nav-item>-->
<!--          <b-nav-item v-if="isTokenPresent" @click="logout">로그아웃</b-nav-item>-->
<!--          <b-nav-item v-if="isTokenPresent" to="/mypage">마이페이지</b-nav-item>-->


<!--        </b-navbar-nav>-->
<!--      </b-collapse>-->
<!--    </b-navbar>-->
<!--  </div>-->
<!--</template>-->
<!--<script>-->

<!--export default {-->

<!--  name: "HeaderNav",-->
<!--  computed: {-->
<!--    isTokenPresent() {-->
<!--      return !!localStorage.getItem('token');-->
<!--    }-->
<!--  },-->
<!--  methods: {-->
<!--    logout() {-->
<!--      localStorage.removeItem('token'); // 로컬 스토리지에서 토큰 삭제-->
<!--      this.$router.push('/account/login'); // 로그인 페이지로 리다이렉트-->
<!--    },-->
<!--  }-->


<!--};-->
<!--</script>-->

<template>
  <div>
    <b-navbar :key="navKey" toggleable="lg" type="dark" variant="info">
      <b-navbar-brand to="/">VFMarket</b-navbar-brand>

      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <b-nav-item v-if="isTokenPresent" to="/product/registration">상품등록</b-nav-item>
          <b-nav-item to="/product/list">상품리스트</b-nav-item>
        </b-navbar-nav>

        <b-navbar-nav class="ml-auto">
          <b-nav-form>
            <b-form-input size="sm" class="mr-sm-2" placeholder="Search"></b-form-input>
            <b-button size="sm" my-2 my-sm-0 type="submit">Search</b-button>
          </b-nav-form>

          <b-nav-item v-if="!isTokenPresent" to="/account/login">로그인</b-nav-item>
          <b-nav-item v-if="!isTokenPresent" to="/account/join">회원가입</b-nav-item>
          <b-nav-item v-if="isTokenPresent" @click="logout">로그아웃</b-nav-item>
          <b-nav-item v-if="isTokenPresent" to="/user/mypage">마이페이지</b-nav-item>


        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
  </div>
</template>
<script>

export default {
  name: "HeaderNav",
  data() {
    return {
      navKey: 0, // 컴포넌트를 강제로 업데이트하기 위한 키
      token: localStorage.getItem('accessToken'), // 토큰을 데이터로 관리합니다.
    };
  },
  computed: {
    isTokenPresent() {
      return this.token !== null;
    }
  },
  methods: {
    setToken(token) {
      localStorage.setItem('accessToken', token);
      this.token = token;
    },
    removeToken() {
      localStorage.removeItem('accessToken');
      this.token = null;
    },
    logout() {
      this.removeToken();
      localStorage.removeItem('refreshToken');
      if (this.$router.currentRoute.path !== '/account/login') {
        this.$router.push('/account/login');
      } else {
        this.$router.push('/');
      }
      this.navKey++;
    },
  },
};
</script>