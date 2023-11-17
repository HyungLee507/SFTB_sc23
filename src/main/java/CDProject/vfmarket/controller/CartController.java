package CDProject.vfmarket.controller;

import CDProject.vfmarket.global.jwt.TokenValueProvider;
import CDProject.vfmarket.service.CartService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
        } catch (Exception e) {
            return new ResponseEntity("장바구니 저장 실패", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("장바구니 저장 성공", HttpStatus.OK);
    }

    @DeleteMapping("/delete-item")
    public ResponseEntity deleteItem(@RequestHeader("Authorization") String token, @RequestParam Long itemId) {
        String trim = token.replace("Bearer ", "");
        Claims claims = tokenValueProvider.extractClaims(trim);
        long userId = Long.parseLong(claims.get("userId").toString());
        try {
//            cartService.saveItemToCart(userId, itemId);
            cartService.deleteItemInCart(itemId, userId);
        } catch (Exception e) {
            return new ResponseEntity("장바구니 저장 실패", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("장바구니 저장 성공", HttpStatus.OK);
    }
//    @GetMapping("/cart-items")
//    public List<> cartItems(Long memberId){
//
//    }
}
