package CDProject.vfmarket.controller;

import static CDProject.vfmarket.global.AuthenticationUserId.getAuthenticatedUser;

import CDProject.vfmarket.domain.dto.itemDTO.CartItemDto;
import CDProject.vfmarket.exceptions.AlreadySavedItem;
import CDProject.vfmarket.exceptions.ItemNotFoundException;
import CDProject.vfmarket.exceptions.UserNotFoundException;
import CDProject.vfmarket.service.CartService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CartController {
    
    private final CartService cartService;

    @PostMapping("/save-item")
    public ResponseEntity<?> saveItem(@RequestParam Long itemId)
            throws Exception {
        Long userId = getAuthenticatedUser();
        try {
            cartService.saveItemToCart(userId, itemId);
        } catch (AlreadySavedItem e) {
            return new ResponseEntity("장바구니 저장 실패 - 이미 저장된 상품입니다.", HttpStatus.BAD_REQUEST);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity("장바구니 저장 실패 - 상품을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException e) {
            return new ResponseEntity("장바구니 저장 실패 - 해당 유저를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("장바구니 저장 성공", HttpStatus.OK);
    }

    @DeleteMapping("/delete-item")
    public ResponseEntity<?> deleteItem(@RequestParam Long itemId) {
        Long userId = getAuthenticatedUser();
        try {
            cartService.deleteItemInCart(itemId, userId);
        } catch (Exception e) {
            return new ResponseEntity("장바구니 저장 실패", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("장바구니 저장 성공", HttpStatus.OK);
    }

    @GetMapping("/cart-items")
    public List<CartItemDto> cartItems() {
        Long userId = getAuthenticatedUser();
        return cartService.cartItems(userId);
    }
}
