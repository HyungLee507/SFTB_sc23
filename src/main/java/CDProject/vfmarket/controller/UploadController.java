package CDProject.vfmarket.controller;

import CDProject.vfmarket.domain.dto.itemDTO.ImageUpdateForm;
import CDProject.vfmarket.domain.dto.itemDTO.ItemFormDto;
import CDProject.vfmarket.service.UploadService;
import java.io.IOException;
import java.net.MalformedURLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final UploadService uploadService;
    @Value("${imagePath}")
    private String imagePath;

    @PostMapping("product/item-upload")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> register(@RequestHeader("Authorization") String token,
                                      @ModelAttribute ItemFormDto itemFormDto)
            throws IOException, NoSuchFieldException {
        uploadService.saveItem(token, itemFormDto);
        return new ResponseEntity<>("상품 등록 성공", HttpStatus.OK);
    }

    @GetMapping("/product/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        log.info("file name is {}", filename);
        return new UrlResource("file:"
                + imagePath + filename);
    }

    @PostMapping("/add-image")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addImage(@ModelAttribute ImageUpdateForm imageUpdateForm)
            throws IOException, NoSuchFieldException {
        uploadService.updateImage(imageUpdateForm);
        return new ResponseEntity<>("이미지 추가 성공", HttpStatus.OK);
    }


}
