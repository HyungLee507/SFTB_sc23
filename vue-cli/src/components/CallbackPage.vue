<template>
    <div>
        <p>Naver Login Access Pages</p>
    </div>
</template>

<script>
export default {
    mounted() {
        new window.naver_id_login("Client Id", "callback URL");
        //console.log("access token", naver_id_login.getAccessToken()); // 정상적 로그인이 된 경우 access token값 출력
    },
};
</script>