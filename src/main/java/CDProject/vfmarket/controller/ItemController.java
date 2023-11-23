package CDProject.vfmarket.controller;

import CDProject.vfmarket.domain.dto.itemDTO.ItemDetailDto;
import CDProject.vfmarket.domain.dto.itemDTO.ItemViewDto;
import CDProject.vfmarket.domain.dto.itemDTO.SalesItemDto;
import CDProject.vfmarket.global.jwt.TokenValueProvider;
import CDProject.vfmarket.service.ItemService;
import io.jsonwebtoken.Claims;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
//@EnableMethodSecurity
public class ItemController {

    private final ItemService itemService;
    private final TokenValueProvider tokenValueProvider;

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
        log.info("value is = {}", uniqueItemsMap.values());
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

    @GetMapping("/salesItems")
    public List<SalesItemDto> salesItems(@RequestHeader("Authorization") String token) {
        String trim = token.replace("Bearer ", "");
        Claims claims = tokenValueProvider.extractClaims(trim);
        long sellerId = Long.parseLong(claims.get("userId").toString());
        List<SalesItemDto> salesItems = itemService.getSalesItems(sellerId);
        log.info("sales Items ={}", salesItems);
        return salesItems;
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
