package CDProject.vfmarket.controller;

import CDProject.vfmarket.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequiredArgsConstructor
public class VFController {

    private final ItemService itemService;
    @PostMapping("/vf/productimg")
    public ResponseEntity sendProductImg(Long productId) throws IOException {
        String prodImgUrl = itemService.itemDetailInfo(productId).getImages().get(0);
        // 전송할 파일 정보
        File file = new File("C:/sw-capstone/images/"+prodImgUrl);

        // Flask ngrok base url. Flask는 colab에서 돌아간다.
        String ngrokBase = "https://18dc-34-90-87-63.ngrok-free.app/";
        // Flask 백엔드의 엔드포인트 URL
        String url = ngrokBase + "getprod";


        // MultiValueMap에 파일 데이터 추가
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("product", new FileSystemResource(file));
        body.add("prodId", productId.toString());

        // Request Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("ngrok-skip-browser-warning", "true");

        // Request Entity 생성
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // RestTemplate을 사용하여 POST 요청 전송
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        // 응답 결과 확인
//        if (response.getStatusCode().is2xxSuccessful()) {
//            return new ResponseEntity<>(HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
        return new ResponseEntity<>(response.getStatusCode());

    }
}
