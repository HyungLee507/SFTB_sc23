package CDProject.vfmarket.service;

import CDProject.vfmarket.domain.entity.Item;
import CDProject.vfmarket.domain.entity.StyleShot;
import CDProject.vfmarket.domain.entity.User;
import CDProject.vfmarket.repository.ItemRepository;
import CDProject.vfmarket.repository.StyleShotRepository;
import CDProject.vfmarket.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VFService {


    private final ItemRepository itemRepository;

    private final UserRepository userRepository;
    private final StyleShotRepository styleShotRepository;
    @Value("${imagePath}")
    private String imagePath;


    public void saveStyleShot(Long prodId, Long userId, byte[] resultFile) {

        String uuid = UUID.randomUUID().toString();
        String styleshotName = uuid + "_" + prodId.toString() + "_" + userId.toString() + ".png";
        // 받은 파일 데이터를 파일로 저장
        File receivedFile = new File(imagePath + styleshotName);
        try (FileOutputStream outputStream = new FileOutputStream(receivedFile)) {
            outputStream.write(resultFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Optional<User> buyer = userRepository.findById(userId);
        Optional<Item> product = itemRepository.findById(prodId);
        User user = null;
        Item item = null;
        if (buyer.isPresent()) {
            user = buyer.get();
        }
        if (product.isPresent()) {
            item = product.get();
        }
        StyleShot styleShot = StyleShot.builder()
                .user(user)
                .item(item)
                .savedStyleShot(styleshotName)
                .build();
        styleShotRepository.save(styleShot);
    }

    public List<StyleShot> getStyleShots(Long userId) {
        return styleShotRepository.findByUser_Id(userId);
    }

    public void deleteStyleShots(Long styleShotId) {
        styleShotRepository.deleteById(styleShotId);
    }
}
