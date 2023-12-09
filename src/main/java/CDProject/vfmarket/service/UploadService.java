package CDProject.vfmarket.service;

import static CDProject.vfmarket.domain.entity.ItemStatus.FOR_SALE;

import CDProject.vfmarket.domain.dto.itemDTO.ImageUpdateForm;
import CDProject.vfmarket.domain.dto.itemDTO.ItemFormDto;
import CDProject.vfmarket.domain.entity.Image;
import CDProject.vfmarket.domain.entity.Item;
import CDProject.vfmarket.domain.entity.User;
import CDProject.vfmarket.global.jwt.TokenValueProvider;
import CDProject.vfmarket.repository.ImageRepository;
import CDProject.vfmarket.repository.ItemRepository;
import CDProject.vfmarket.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.asm.ex.NoSuchFieldException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UploadService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final TokenValueProvider tokenValueProvider;

    private final ImageRepository imageRepository;

    // 저장할 파일 위치 생성
    @Value("${imagePath}")
    private String uploadFolder;

    public void saveItem(String token, ItemFormDto itemFormDto) throws NoSuchFieldException {

        List<String> imageList = new ArrayList<>();
        for (MultipartFile uploadFile : itemFormDto.getImages()) {
            try {
                String uploadFileName = uploadFile.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();
                uploadFileName = uuid + "_" + uploadFileName;
                File saveFile = new File(uploadFolder, uploadFileName);
                uploadFile.transferTo(saveFile);
                imageList.add(uploadFileName);
            } catch (Exception e) {
                log.info("file error is {}", e.getMessage());
                throw new NoSuchFieldException("해당 파일을 찾을 수 없습니다.");
            }
        }

        try {
            Long userId = tokenValueProvider.extractUserId(token);
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
                    .reviewSubmitted(false)
                    .build();

            for (String fileName : imageList) {
                List<Image> images = uploadItem.getImages();
                images.add(new Image(fileName, uploadItem));
            }
            itemRepository.save(uploadItem);
        } catch (Exception e) {
            log.info("token error is {}", e.getMessage());
        }
    }

    public void updateImage(ImageUpdateForm imageUpdateFormDto) throws NoSuchFieldException {

        Item item = itemRepository.findById(imageUpdateFormDto.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("해당 아이템을 찾을 수 없습니다."));

        try {
            MultipartFile uploadFile = imageUpdateFormDto.getImage();
            String uploadFileName = uploadFile.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            uploadFileName = uuid + "_" + uploadFileName;
            File saveFile = new File(uploadFolder, uploadFileName);
            uploadFile.transferTo(saveFile);
            imageRepository.save(new Image(uploadFileName, item));
        } catch (Exception e) {
            log.info("item image Update error is {}", e.getMessage());
        }
    }
}
