package CDProject.vfmarket.controller;

import CDProject.vfmarket.domain.dto.OrderDTO.OrderViewDto;
import CDProject.vfmarket.domain.dto.OrderDTO.SoldItemViewDto;
import CDProject.vfmarket.global.jwt.TokenValueProvider;
import CDProject.vfmarket.payment.RefundService;
import CDProject.vfmarket.service.OrderService;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final RefundService refundService;

    private final TokenValueProvider tokenValueProvider;

    @GetMapping("/orderItems")
    public List<OrderViewDto> orderHistory(@RequestHeader("Authorization") String token) {
        String trim = token.replace("Bearer ", "");
        Claims claims = tokenValueProvider.extractClaims(trim);
        long userId = Long.parseLong(claims.get("userId").toString());
        return orderService.getOrderHistory(userId);
    }

    @GetMapping("/soldItems")
    public List<SoldItemViewDto> soldItemHistory(@RequestHeader("Authorization") String token) {
        String trim = token.replace("Bearer ", "");
        Claims claims = tokenValueProvider.extractClaims(trim);
        long userId = Long.parseLong(claims.get("userId").toString());
        return orderService.getSoldItemHistory(userId);
    }

    @PutMapping("/refundItem/{orderId}")
    public void canceledOrder(@RequestHeader("Authorization") String token, @PathVariable Long orderId)
            throws IOException {
        String trim = token.replace("Bearer ", "");
        Claims claims = tokenValueProvider.extractClaims(trim);
        long userId = Long.parseLong(claims.get("userId").toString());
        refundService.canceledOrder(orderId);
    }


}
