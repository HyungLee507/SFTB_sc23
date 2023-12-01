package CDProject.vfmarket.service;

import static CDProject.vfmarket.domain.entity.ItemStatus.FOR_SALE;

import CDProject.vfmarket.domain.dto.itemDTO.ItemFormDto;
import CDProject.vfmarket.domain.entity.Image;
import CDProject.vfmarket.domain.entity.Item;
import CDProject.vfmarket.domain.entity.StyleShot;
import CDProject.vfmarket.domain.entity.User;
import CDProject.vfmarket.global.jwt.TokenValueProvider;
import CDProject.vfmarket.repository.ItemRepository;
import CDProject.vfmarket.repository.StyleShotRepository;
import CDProject.vfmarket.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UploadService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final StyleShotRepository styleShotRepository;

    private final TokenValueProvider tokenValueProvider;

    // 저장할 파일 위치 생성
    private final String uploadFolder = "C:/sw-capstone/images";
    private final String styleshotFolder = "C:/sw-capstone/styleshots";

    public void saveItem(String token, ItemFormDto itemFormDto) throws NoSuchFieldException {

        List<String> imageList = new ArrayList<>();
        for (MultipartFile uploadFile : itemFormDto.getImages()) {
            try {
                String uploadFileName = uploadFile.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();
                uploadFileName = uuid + "_" + uploadFileName;
                File saveFile = new File(uploadFolder, uploadFileName);
                log.info("uploadFileName is {}", uploadFileName);
                uploadFile.transferTo(saveFile);
                imageList.add(uploadFileName);
                log.info("imageList is {}", imageList);
            } catch (Exception e) {
                log.info("file error is {}", e.getMessage());
                throw new NoSuchFieldException("해당 파일을 찾을 수 없습니다.");
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
            Optional<User> seller = userRepository.findById(userId);
            String sellerName = seller.get().getName();
            Item uploadItem = Item.builder()
                    .sellerId(userId)
                    .itemName(itemFormDto.getName())
                    .price(itemFormDto.getPrice())
                    .sellerName(sellerName)
                    .shoeSize(itemFormDto.getShoeSize())
                    .category(itemFormDto.getCategory())
                    .description(itemFormDto.getDescription())
                    .status(FOR_SALE)
                    .build();
            log.info("item data is {}", uploadItem.getItemName());
            for (String fileName : imageList) {
                List<Image> images = uploadItem.getImages();
                images.add(new Image(fileName, uploadItem));
            }
            itemRepository.save(uploadItem);
        } catch (Exception e) {
            log.info("token error is {}", e.getMessage());
        }

    }

    public void saveStyleShot(Long prodId, Long userId, byte[] resultFile) throws NoSuchFieldException {

        String uuid = UUID.randomUUID().toString();
        String styleshotName = uuid + "_" + prodId.toString() + "_" + userId.toString() + ".png";
        // 받은 파일 데이터를 파일로 저장
        File receivedFile = new File(styleshotFolder, styleshotName);
        try (FileOutputStream outputStream = new FileOutputStream(receivedFile)) {
            outputStream.write(resultFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Optional<User> buyer = userRepository.findById(userId);
        Optional<Item> product = itemRepository.findById(prodId);
        User user = null;
        Item item = null;
        if (buyer.isPresent())
            user = buyer.get();
        if (product.isPresent())
            item = product.get();
        StyleShot styleShot = StyleShot.builder()
                .user(user)
                .item(item)
                .savedStyleShot(styleshotName)
                .build();
        styleShotRepository.save(styleShot);
    }

}
