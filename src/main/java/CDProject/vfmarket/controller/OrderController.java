package CDProject.vfmarket.controller;

import static CDProject.vfmarket.global.AuthenticationUserId.getAuthenticatedUser;

import CDProject.vfmarket.domain.dto.OrderDTO.OrderDetailSaveDTO;
import CDProject.vfmarket.domain.dto.OrderDTO.OrderDoneDTO;
import CDProject.vfmarket.domain.dto.OrderDTO.OrderViewDto;
import CDProject.vfmarket.domain.dto.OrderDTO.ProgressOrderDto;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class OrderController {

    private final OrderService orderService;

    private final RefundService refundService;

    // 거래중 내역 조회
    @GetMapping("/orderItems")
    public List<OrderViewDto> orderHistory() {
        Long userId = getAuthenticatedUser();
        return orderService.getOrderHistory(userId);
    }

    // 구매 완료 내역 조회
    @GetMapping("/ordersDone")
    public List<OrderDoneDTO> orderDoneHistory() {
        Long buyerId = getAuthenticatedUser();
        return orderService.getDoneOrders(buyerId);
    }


    //판매 완료 내역 조회
    @GetMapping("/soldItems")
    public List<SoldItemViewDto> soldItemHistory() {
        Long userId = getAuthenticatedUser();
        return orderService.getSoldItemHistory(userId);
    }

    // 거래 취소
    @PutMapping("/refundItem/{orderId}")
    public void canceledOrder(@PathVariable Long orderId)
            throws IOException {
        refundService.canceledOrder(orderId);
    }

    //거래 중 목록 조회
    @GetMapping("/tradeItems")
    public List<ProgressOrderDto> progressingOrder() {
        Long sellerId = getAuthenticatedUser();
        return orderService.getProgressOrders(sellerId);
    }

    @PutMapping("/product-bill")
    public void updateBillingNumber(@RequestBody OrderDetailSaveDTO orderDetailSaveDTO) {
        orderService.updateBillingNumber(orderDetailSaveDTO);
    }

}
