package CDProject.vfmarket.controller;

import CDProject.vfmarket.domain.dto.itemDTO.CartItemDto;
import CDProject.vfmarket.exceptions.AlreadySavedItem;
import CDProject.vfmarket.exceptions.ItemNotFoundException;
import CDProject.vfmarket.exceptions.UserNotFoundException;
import CDProject.vfmarket.global.jwt.TokenValueProvider;
import CDProject.vfmarket.service.CartService;
import io.jsonwebtoken.Claims;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final TokenValueProvider tokenValueProvider;

    @PostMapping("/save-item")
    public ResponseEntity saveItem(@RequestHeader("Authorization") String token, @RequestParam Long itemId)
            throws Exception {
        String trim = token.replace("Bearer ", "");
        Claims claims = tokenValueProvider.extractClaims(trim);
        long userId = Long.parseLong(claims.get("userId").toString());
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
    public ResponseEntity deleteItem(@RequestHeader("Authorization") String token, @RequestParam Long itemId) {
        String trim = token.replace("Bearer ", "");
        Claims claims = tokenValueProvider.extractClaims(trim);
        long userId = Long.parseLong(claims.get("userId").toString());
        try {
            cartService.deleteItemInCart(itemId, userId);
        } catch (Exception e) {
            return new ResponseEntity("장바구니 저장 실패", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("장바구니 저장 성공", HttpStatus.OK);
    }

    @GetMapping("/cart-items")
    public List<CartItemDto> cartItems(@RequestHeader("Authorization") String token) {
        String trim = token.replace("Bearer ", "");
        Claims claims = tokenValueProvider.extractClaims(trim);
        long userId = Long.parseLong(claims.get("userId").toString());
        return cartService.cartItems(userId);
    }
}
