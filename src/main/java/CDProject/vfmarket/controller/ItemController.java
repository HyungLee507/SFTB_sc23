package CDProject.vfmarket.controller;

import CDProject.vfmarket.domain.dto.itemDTO.ItemDetailDto;
import CDProject.vfmarket.domain.dto.itemDTO.ItemViewDto;
import CDProject.vfmarket.service.ItemService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
//@EnableWebSecurity
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/product-list")
//    @PreAuthorize("isAuthenticated()")
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

    //    @PutMapping("/product-update")
//    public void deleteItem(@RequestHeader("Authorization") String token , @ResponseBody ItemUpdateDto){
//
//    }
    @GetMapping("/product-list/{itemId}")
    public ItemDetailDto itemDetail(@PathVariable Long itemId) {
        return itemService.itemDetailInfo(itemId);
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

}
