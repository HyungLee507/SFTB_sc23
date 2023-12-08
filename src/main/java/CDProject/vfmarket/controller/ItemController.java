package CDProject.vfmarket.controller;

import static CDProject.vfmarket.global.AuthenticationUserId.getAuthenticatedUser;

import CDProject.vfmarket.domain.dto.itemDTO.ItemDetailDto;
import CDProject.vfmarket.domain.dto.itemDTO.ItemTextUpdateForm;
import CDProject.vfmarket.domain.dto.itemDTO.ItemViewDto;
import CDProject.vfmarket.domain.dto.itemDTO.SalesItemDto;
import CDProject.vfmarket.service.ImageService;
import CDProject.vfmarket.service.ItemService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@EnableMethodSecurity
public class ItemController {

    private final ItemService itemService;
    private final ImageService imageService;

    @GetMapping("/product-list")
    public List<ItemViewDto> productList() {
        List<ItemViewDto> allItemsWithImages = itemService.productList();
        Map<Long, ItemViewDto> uniqueItemsMap = new HashMap<>();
        for (ItemViewDto dto : allItemsWithImages) {
            if (!uniqueItemsMap.containsKey(dto.getId())) {
                uniqueItemsMap.put(dto.getId(), dto);
            }
        }
        return new ArrayList<>(uniqueItemsMap.values());
    }

    @PutMapping("/updateItem/text/{itemId}")
    @PreAuthorize("isAuthenticated()")
    public void textUpdate(@RequestBody ItemTextUpdateForm updateForm) {
        itemService.updateText(updateForm);
    }

    @GetMapping("/product-detail/{itemId}")
    public ItemDetailDto itemDetail(@PathVariable Long itemId) {
        return itemService.itemDetailInfo(itemId);
    }

    @GetMapping("/salesItems")
    @PreAuthorize("isAuthenticated()")
    public List<SalesItemDto> salesItems() {
        Long sellerId = getAuthenticatedUser();
        return itemService.getSalesItems(sellerId);
    }


    @PutMapping("/product-remove")
    public String deleteItems(@RequestParam Long itemId) {
        try {
            itemService.deleteItem(itemId);
            return "아이템 삭제 성공";
        } catch (Exception e) {
            return "아이템 삭제 실패";
        }
    }

    @PutMapping("/updateItem/image/{imageId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateImage(@PathVariable Long imageId, @RequestParam("file") MultipartFile file) {
        try {
            imageService.updateImage(imageId, file);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File storage error");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
