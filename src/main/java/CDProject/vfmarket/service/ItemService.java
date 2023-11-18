package CDProject.vfmarket.service;


import CDProject.vfmarket.domain.dto.itemDTO.ItemDetailDto;
import CDProject.vfmarket.domain.dto.itemDTO.ItemUpdateDto;
import CDProject.vfmarket.domain.dto.itemDTO.ItemViewDto;
import CDProject.vfmarket.domain.entity.Image;
import CDProject.vfmarket.domain.entity.Item;
import CDProject.vfmarket.domain.entity.ItemStatus;
import CDProject.vfmarket.exceptions.ResourceNotFoundException;
import CDProject.vfmarket.repository.ImageRepository;
import CDProject.vfmarket.repository.ItemRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;

    public List<ItemViewDto> productList() {
        return itemRepository.findAllItemListDto();
    }

    public Item updateItem(Long id, ItemUpdateDto itemUpdateDto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
        if (!itemUpdateDto.getName().isBlank()) {
            item.setItemName(itemUpdateDto.getName());
        }
        if (!itemUpdateDto.getDescription().isBlank()) {
            item.setDescription(itemUpdateDto.getDescription());
        }
        if (itemUpdateDto.getPrice() != null) {
            item.setPrice(itemUpdateDto.getPrice());
        }
        if (itemUpdateDto.getShoeSize().describeConstable().isPresent()) {
            item.setPrice(itemUpdateDto.getPrice());
        }
        List<MultipartFile> images = itemUpdateDto.getImages();

        return itemRepository.save(item);
    }

//    public void deleteItem(Long id) {
//        Optional<Item> item = itemRepository.findById(id);
//        if (item.isPresent()) {
//            itemRepository.deleteById(item.get().getId());
//            log.info("회원 탈퇴 성공!! db 삭제");
//        }
//        if (item.isEmpty()) {
//            log.info("회원 정보 가져오기 실패!");
//        }
//    }

    public ItemDetailDto itemDetailInfo(Long itemId) {
        Optional<Item> findItem = itemRepository.findById(itemId);
        List<Image> imageList = imageRepository.findImagesByItemId(itemId);
        if (findItem.isEmpty() || imageList.isEmpty()) {
            return null;
        }
        List<String> images = new ArrayList<>();
        for (Image image : imageList) {
            images.add(image.getFileName());
        }

//        if (findItem.get().getImage1() != null) {
//            images.add(findItem.get().getImage1());
//        }
//        if (findItem.get().getImage2() != null) {
//            images.add(findItem.get().getImage2());
//        }
//        if (findItem.get().getImage3() != null) {
//            images.add(findItem.get().getImage3());
//        }
//        if (findItem.get().getImage4() != null) {
//            images.add(findItem.get().getImage4());
//        }
//        if (findItem.get().getImage5() != null) {
//            images.add(findItem.get().getImage5());
//        }
        ItemDetailDto itemDetailDto = new ItemDetailDto();
        itemDetailDto.setId(findItem.get().getId());
        itemDetailDto.setPrice(findItem.get().getPrice());
        itemDetailDto.setName(findItem.get().getItemName());
        itemDetailDto.setSellerName(findItem.get().getSellerName());
        itemDetailDto.setShoeSize(findItem.get().getShoeSize());
        itemDetailDto.setDescription(findItem.get().getDescription());
        itemDetailDto.setCategory(findItem.get().getCategory());
        itemDetailDto.setImages(images);
        return itemDetailDto;
    }

    public void deleteItem(Long itemId) throws Exception {
        Optional<Item> findItem = itemRepository.findById(itemId);
        if (findItem.isEmpty()) {
            throw new Exception("해당 아이템을 찾을 수 없습니다.");
        }
        findItem.get().setStatus(ItemStatus.STOP_SELLING);
    }
}
