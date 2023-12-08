package CDProject.vfmarket.service;


import CDProject.vfmarket.domain.dto.itemDTO.ItemDetailDto;
import CDProject.vfmarket.domain.dto.itemDTO.ItemTextUpdateForm;
import CDProject.vfmarket.domain.dto.itemDTO.ItemUpdateDto;
import CDProject.vfmarket.domain.dto.itemDTO.ItemViewDto;
import CDProject.vfmarket.domain.dto.itemDTO.SalesItemDto;
import CDProject.vfmarket.domain.entity.Image;
import CDProject.vfmarket.domain.entity.Item;
import CDProject.vfmarket.domain.entity.ItemStatus;
import CDProject.vfmarket.exceptions.ResourceNotFoundException;
import CDProject.vfmarket.repository.ImageRepository;
import CDProject.vfmarket.repository.ItemRepository;
import CDProject.vfmarket.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

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

    public void updateText(ItemTextUpdateForm itemTextUpdateForm) {
        Item item = itemRepository.findById(itemTextUpdateForm.getItemId()).orElseThrow(
                () -> new EntityNotFoundException("Item not found with id: " + itemTextUpdateForm.getItemId()));
        item.setDescription(itemTextUpdateForm.getDescription());
        item.setItemName(itemTextUpdateForm.getName());
        item.setCategory(itemTextUpdateForm.getCategory());
        item.setShoeSize(itemTextUpdateForm.getShoeSize());
        item.setPrice(itemTextUpdateForm.getPrice());
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
        List<Long> imageIds = new ArrayList<>();
        for (Image image : imageList) {
            images.add(image.getFileName());
            imageIds.add(image.getId());
        }
        return getItemDetailDto(findItem, images, imageIds);
    }

    @NotNull
    private static ItemDetailDto getItemDetailDto(Optional<Item> findItem, List<String> images, List<Long> imageIds) {
        ItemDetailDto itemDetailDto = new ItemDetailDto();
        itemDetailDto.setId(findItem.get().getId());
        itemDetailDto.setPrice(findItem.get().getPrice());
        itemDetailDto.setName(findItem.get().getItemName());
        itemDetailDto.setSellerName(findItem.get().getSellerName());
        itemDetailDto.setShoeSize(findItem.get().getShoeSize());
        itemDetailDto.setDescription(findItem.get().getDescription());
        itemDetailDto.setCategory(findItem.get().getCategory());
        itemDetailDto.setImages(images);
        itemDetailDto.setStatus(findItem.get().getStatus());
        itemDetailDto.setImageIds(imageIds);
        itemDetailDto.setSellerId(findItem.get().getSellerId());
        return itemDetailDto;
    }

    public List<SalesItemDto> getSalesItems(Long sellerId) {
        List<Item> items = itemRepository.findAllBySellerId(sellerId);
        return items.stream()
                .map(item -> {
                    String firstImageFileName =
                            item.getImages().isEmpty() ? null : item.getImages().get(0).getFileName();
                    return new SalesItemDto(
                            item.getId(),
                            item.getPrice(),
                            item.getItemName(),
                            firstImageFileName
                    );
                })
                .collect(Collectors.toList());
    }

    ;

    public void deleteItem(Long itemId) throws Exception {
        Optional<Item> findItem = itemRepository.findById(itemId);
        if (findItem.isEmpty()) {
            throw new Exception("해당 아이템을 찾을 수 없습니다.");
        }
        findItem.get().setStatus(ItemStatus.STOP_SELLING);
    }
}
