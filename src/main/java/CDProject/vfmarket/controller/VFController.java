package CDProject.vfmarket.controller;

import CDProject.vfmarket.domain.dto.StyleShotDto;
import CDProject.vfmarket.domain.dto.VFFormDTO;
import CDProject.vfmarket.domain.entity.Item;
import CDProject.vfmarket.domain.entity.StyleShot;
import CDProject.vfmarket.global.jwt.TokenValueProvider;
import CDProject.vfmarket.service.ItemService;
import CDProject.vfmarket.service.VFService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VFController {

    private final ItemService itemService;
    private final VFService vfService;
    private final TokenValueProvider tokenValueProvider;
    private final String flaskHost = "https://762b-35-247-34-254.ngrok-free.app/";
    @PostMapping("/vf/productimg")
    public ResponseEntity sendProductImg(@RequestHeader("Authorization") String token, Long productId) {
        if (token == null) {
            return new ResponseEntity("로그인을 해야 가상피팅을 진행할 수 있습니다.", HttpStatus.NO_CONTENT);
        }

        String prodImgUrl = itemService.itemDetailInfo(productId).getImages().get(0);
        // 전송할 파일 정보
        File file = new File("C:/sw-capstone/images/"+prodImgUrl);

        // Flask ngrok base url. Flask는 colab에서 돌아간다.
        // Flask 백엔드의 엔드포인트 URL
        String url = flaskHost + "getprod";
        Long userId = null;
        try {
            log.info("token value is {}", token);

            String trim = token.replace("Bearer ", "");
            log.info("trim value is {}", trim);
            Claims claims = tokenValueProvider.extractClaims(trim);
            log.info("claims is {}", claims);
            userId = Long.parseLong(claims.get("userId").toString());
        } catch (Exception e) {
            log.info("token error is {}", e.getMessage());
        }


        // MultiValueMap에 파일 데이터 추가
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("product", new FileSystemResource(file));
        body.add("prodId", productId.toString());
        body.add("userId", Objects.requireNonNull(userId).toString());

        // Request Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("ngrok-skip-browser-warning", "true");

        // Request Entity 생성
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // RestTemplate을 사용하여 POST 요청 전송
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        String data = response.getBody();

        return new ResponseEntity<>(data, response.getStatusCode());

    }

    @PostMapping("/vf/humanimg")
    public ResponseEntity sendHumanImage(@ModelAttribute VFFormDTO vfFormDTO) {
        log.info("실행 시작");
        String url = flaskHost + "showimage";
        MultipartFile humanImage = vfFormDTO.getImage();
        String dirname = vfFormDTO.getDirname();

        File image = new File(Objects.requireNonNull(humanImage.getOriginalFilename()));

        try (FileOutputStream fileOutputStream = new FileOutputStream(image)) {
            FileCopyUtils.copy(humanImage.getInputStream(), fileOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // MultiValueMap에 파일 데이터 추가
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new FileSystemResource(image));
        body.add("dirname", dirname);

        // Request Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("ngrok-skip-browser-warning", "true");

        // Request Entity 생성
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // RestTemplate을 사용하여 POST 요청 전송
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        String data = response.getBody();

        return new ResponseEntity<>(data, response.getStatusCode());
    }

    @PostMapping("/vf/savestyle")
    public ResponseEntity saveStyleShot(@RequestHeader("Authorization") String token,
                                    @RequestParam("dirname") String dirname,
                                    @RequestParam("prodId") Long prodId) {
        log.info("dirname: {}", dirname);
        log.info("prodId: {}", prodId);
        if (token == null) {
            return new ResponseEntity("로그인을 해야 가상피팅 결과를 저장할 수 있습니다.", HttpStatus.NO_CONTENT);
        }

        String url = flaskHost + "export";
        Long userId = null;
        try {
            log.info("token value is {}", token);

            String trim = token.replace("Bearer ", "");
            log.info("trim value is {}", trim);
            Claims claims = tokenValueProvider.extractClaims(trim);
            log.info("claims is {}", claims);
            userId = Long.parseLong(claims.get("userId").toString());
        } catch (Exception e) {
            log.info("token error is {}", e.getMessage());
        }

        // MultiValueMap에 파일 데이터 추가
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("dirname", dirname);
        body.add("prodId", prodId.toString());
        body.add("userId", Objects.requireNonNull(userId).toString());

        // Request Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("ngrok-skip-browser-warning", "true");

        // Request Entity 생성
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // RestTemplate을 사용하여 POST 요청 전송
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.postForEntity(url, requestEntity, byte[].class);

        // 응답 데이터 받기
        byte[] resultByte = response.getBody();

        vfService.saveStyleShot(prodId, userId, resultByte);

        return new ResponseEntity<>("이미지 저장에 성공했습니다.", HttpStatus.OK);
    }

    @GetMapping("/vf/viewstyle")
    public List<StyleShotDto> viewStyleShots(@RequestHeader("Authorization") String token){
        Long userId = null;
        try {
            log.info("token value is {}", token);

            String trim = token.replace("Bearer ", "");
            log.info("trim value is {}", trim);
            Claims claims = tokenValueProvider.extractClaims(trim);
            log.info("claims is {}", claims);
            userId = Long.parseLong(claims.get("userId").toString());
        } catch (Exception e) {
            log.info("token error is {}", e.getMessage());
        }

        Map<Long, StyleShotDto> styleShotsMap = new HashMap<>();
        List<StyleShot> styleShots = vfService.getStyleShots(userId);
        for(StyleShot styleShot : styleShots){
            Long styleShotId = styleShot.getId();
            String styleShotName = styleShot.getSavedStyleShot();
            Item item = styleShot.getItem();
            StyleShotDto styleShotDto = new StyleShotDto(styleShotId, styleShotName, item.getId(), item.getItemName());
            styleShotsMap.put(styleShotDto.getStyleShotId(), styleShotDto);
        }

        return new ArrayList<>(styleShotsMap.values());
    }

    @DeleteMapping("/vf/deletestyle")
    public ResponseEntity deleteStyleShot(@RequestParam("styleShotId") Long styleShotId){
        vfService.deleteStyleShots(styleShotId);
        return new ResponseEntity<>("삭제가 완료되었습니다.", HttpStatus.OK);
    }

    @GetMapping("/vf/{filename}")
    public Resource getImage(@PathVariable String filename) throws MalformedURLException {
        log.info("file name is {}", filename);
        return new UrlResource("file:"
                + "C:/sw-capstone/styleshots/" + filename);
    }
}
