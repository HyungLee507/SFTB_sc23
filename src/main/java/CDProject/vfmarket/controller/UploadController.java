package CDProject.vfmarket.controller;

import CDProject.vfmarket.domain.dto.itemDTO.ItemFormDto;
import CDProject.vfmarket.global.jwt.TokenValueProvider;
import CDProject.vfmarket.repository.ItemRepository;
import CDProject.vfmarket.repository.UserRepository;
import CDProject.vfmarket.service.UploadService;
import java.io.IOException;
import java.net.MalformedURLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UploadController {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final TokenValueProvider tokenValueProvider;
    private final UploadService uploadService;

    @PostMapping("product/item-upload")
    public ResponseEntity register(@RequestHeader("Authorization") String token,
                                   @ModelAttribute ItemFormDto itemFormDto)
            throws IOException, NoSuchFieldException {

        if (token == null) {
            return new ResponseEntity("로그인을 해야 상품을 등록할 수 있습니다.", HttpStatus.NO_CONTENT);
        }
        uploadService.saveItem(token, itemFormDto);
        return new ResponseEntity<>("상품 등록 성공", HttpStatus.OK);
    }

    @GetMapping("/product/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        log.info("file name is {}", filename);
        return new UrlResource("file:"
                + "/Users/leedonghyun/Desktop/images/" + filename);
    }


}
