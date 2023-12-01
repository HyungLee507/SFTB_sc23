package CDProject.vfmarket.controller;

import static CDProject.vfmarket.global.AuthenticationUserId.getAuthenticatedUser;

import CDProject.vfmarket.domain.dto.OrderDTO.OrderViewDto;
import CDProject.vfmarket.domain.dto.OrderDTO.SoldItemViewDto;
import CDProject.vfmarket.payment.RefundService;
import CDProject.vfmarket.service.OrderService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class OrderController {

    private final OrderService orderService;

    private final RefundService refundService;

    @GetMapping("/orderItems")
    public List<OrderViewDto> orderHistory() {
        Long userId = getAuthenticatedUser();
        return orderService.getOrderHistory(userId);
    }

    @GetMapping("/soldItems")
    public List<SoldItemViewDto> soldItemHistory() {
        Long userId = getAuthenticatedUser();
        return orderService.getSoldItemHistory(userId);
    }

    @PutMapping("/refundItem/{orderId}")
    public void canceledOrder(@PathVariable Long orderId)
            throws IOException {
        refundService.canceledOrder(orderId);
    }


}
