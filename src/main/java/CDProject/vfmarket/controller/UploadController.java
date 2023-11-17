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

        // 저장할 파일 위치 생성
//        String uploadFolder = "/Users/leedonghyun/Desktop/images";
//
//        List<String> imageList = new ArrayList<>();
//        for (MultipartFile uploadFile : itemFormDto.getImages()) {
//            try {
//                String uploadFileName = uploadFile.getOriginalFilename();
//                String uuid = UUID.randomUUID().toString();
//                uploadFileName = uuid + "_" + uploadFileName;
//                File saveFile = new File(uploadFolder, uploadFileName);
//                log.info("uploadFileName is {}", uploadFileName);
//                uploadFile.transferTo(saveFile);
//                imageList.add(uploadFileName);
//                log.info("imageList is {}", imageList);
//            } catch (Exception e) {
//                log.info("file error is {}", e.getMessage());
//                return new ResponseEntity(e.getMessage(), HttpStatus.NO_CONTENT);
//            }
//        }
//        log.info("imageList size is {}", imageList.size());
//        try {
//            log.info("token value is {}", token);
//
//            String trim = token.replace("Bearer ", "");
//            log.info("trim value is {}", trim);
//            Claims claims = tokenValueProvider.extractClaims(trim);
//            log.info("claims is {}", claims);
//            log.info("imageList size is {}", imageList.size());
//            long userId = Long.parseLong(claims.get("userId").toString());
//            Optional<User> seller = userRepository.findById(userId);
//            if (seller.isPresent()) {
//                String sellerName = seller.get().getName();
//            }
//            Item uploadItem = Item.builder()
//                    .sellerId(userId)
//                    .itemName(itemFormDto.getName())
//                    .price(itemFormDto.getPrice())
//                    .showSize(itemFormDto.getShoeSize())
//                    .category(itemFormDto.getCategory())
//                    .description(itemFormDto.getDescription())
//                    .status(FOR_SALE)
//                    .build();
//            log.info("item data is {}", uploadItem.getItemName());
//            for (String fileName : imageList) {
//                uploadItem.getImages().add(new Image(fileName, uploadItem));
//            }
////            uploadItem.setImage1(imageList.get(0));
////            uploadItem.setImage2(imageList.get(1));
////            uploadItem.setImage3(imageList.get(2));
//
////            if (imageList.size() >= 4) {
////                uploadItem.getImages().add();
////            }
////            if (imageList.size() == 5) {
////                uploadItem.setImage5(imageList.get(4));
////            }
//            itemRepository.save(uploadItem);
//        } catch (Exception e) {
//            log.info("token error is {}", e.getMessage());
//            return new ResponseEntity(e.getMessage(), HttpStatus.NO_CONTENT);
//        }
        return new ResponseEntity<>("상품 등록 성공", HttpStatus.OK);
    }

    @GetMapping("/product/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        log.info("file name is {}", filename);
        return new UrlResource("file:"
                + "/Users/leedonghyun/Desktop/images/" + filename);
    }


}
