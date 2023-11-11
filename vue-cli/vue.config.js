const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
        transpileDependencies: true,

        // npm run build 타겟 디렉토리 -> 해당 경로에 vue 빌드 출력물 생성됨
        outputDir: '../backend/src/main/resources/static',

        // npm run serve 개발 진행시에 포트가 다르기때문에 프록시 설정
        devServer: {
            proxy: {
                // /api 및 /api/* 요청에 대해 프록시 설정
                // '/api': {
                '/': {
                    target: 'http://localhost:8080',
                    changeOrigin: true,
                    ws:false,
                    
                },
                //서버 프로젝트 포트번호와 동일할 것
            },
        }
    }
)