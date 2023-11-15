package CDProject.vfmarket.controller;

import static CDProject.vfmarket.domain.entity.ItemStatus.FOR_SALE;

import CDProject.vfmarket.domain.dto.itemDTO.ItemFormDto;
import CDProject.vfmarket.domain.entity.Item;
import CDProject.vfmarket.global.jwt.TokenValueProvider;
import CDProject.vfmarket.repository.ItemRepository;
import io.jsonwebtoken.Claims;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UploadController {
    private final ItemRepository itemRepository;
    private final TokenValueProvider tokenValueProvider;

    @PostMapping("product/item-upload")
    public ResponseEntity register(@RequestHeader("Authorization") String token,
                                   @ModelAttribute ItemFormDto itemFormDto)
            throws IOException {
        // 저장할 파일 위치 생성
        String uploadFolder = "/Users/leedonghyun/Desktop/capstone-project";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String str = sdf.format(date);
        String datePath = str.replace("-", File.separator);
        // 파일 날짜 단위로 생성
        File uploadPath = new File(uploadFolder, datePath);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        List<String> imageList = new ArrayList<>();
        for (MultipartFile uploadFile : itemFormDto.getImages()) {
            try {
                String uploadFileName = uploadFile.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();
                uploadFileName = uuid + "_" + uploadFileName;
                File saveFile = new File(uploadPath, uploadFileName);
                uploadFile.transferTo(saveFile);
                imageList.add(uploadPath + uploadFileName);
            } catch (Exception e) {
                log.info("file error is {}", e.getMessage());
                return new ResponseEntity(e.getMessage(), HttpStatus.NO_CONTENT);
            }
        }
        log.info("imageList size is {}", imageList.size());
        try {
            log.info("token value is {}", token);
            String trim = token.replace("Bearer ", "");
            log.info("trim value is {}", trim);
            Claims claims = tokenValueProvider.extractClaims(trim);
            log.info("claims is {}", claims);
            log.info("imageList size is {}", imageList.size());
            long userId = Long.parseLong(claims.get("userId").toString());
            Item uploadItem = Item.builder()
                    .sellerId(userId)
                    .itemName(itemFormDto.getName())
                    .price(itemFormDto.getPrice())
                    .showSize(itemFormDto.getShoeSize())
                    .category(itemFormDto.getCategory())
                    .description(itemFormDto.getDescription())
                    .status(FOR_SALE)
                    .build();
            log.info("item data is {}", uploadItem.getItemName());
            uploadItem.setImage1(imageList.get(0));
            uploadItem.setImage2(imageList.get(1));
            uploadItem.setImage3(imageList.get(2));

            if (imageList.size() >= 4) {
                uploadItem.setImage4(imageList.get(3));
            }
            if (imageList.size() == 5) {
                uploadItem.setImage5(imageList.get(4));
            }
            itemRepository.save(uploadItem);
        } catch (Exception e) {
            log.info("token error is {}", e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("상품 등록 성공", HttpStatus.OK);
    }


}
