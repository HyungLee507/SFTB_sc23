package CDProject.vfmarket.payment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class PaymentService {

//    private final OrderRepository orderRepository;
//    private final OrderProductRepository orderProductRepository;
//    private final ProductRepository productRepository;

//    public void saveOrder(Long userId, List<OrderSaveDto> orderSaveDtos) {
//        OrderSaveDto saveDto = orderSaveDtos.get(0);
//        Orders orders = Orders.builder()
//                .userIdNo(userId)
//                .orderNumber(saveDto.getOrderNumber())
//                .receiverName(saveDto.getReceiverName())
//                .phoneNumber(saveDto.getPhoneNumber())
//                .zipcode(saveDto.getZipcode())
//                .address(saveDto.getAddress())
//                .orderRequired(saveDto.getOrderRequired())
//                .orderStatus(PayStatus.SUCCESS.getStatus())
//                .paymentMethod(saveDto.getPaymentMethod())
//                .build();
//        orderRepository.insertOrder(orders);
//
//        for (OrderSaveDto dto : orderSaveDtos) {
//            int curStock = productRepository.selectProductStock(dto.getProductId());
//            int orderStock = dto.getOrderCount();
//
//            if (curStock - orderStock < 0) {
//                throw new IllegalArgumentException("상품 재고가 부족합니다.");
//            }
//
//            OrderProduct orderProduct = OrderProduct.builder()
//                    .orderId(orders.getOrderId())
//                    .productId(dto.getProductId())
//                    .orderProductPrice(dto.getOrderPrice())
//                    .orderProductCount(dto.getOrderCount())
//                    .orderProductDiscount(dto.getOrderDiscount())
//                    .productImage(dto.getProductImage())
//                    .build();
//            orderProductRepository.insertOrderProduct(orderProduct);
//            productRepository.updateProductStock(dto.getProductId(), dto.getOrderCount());
//        }
//    }


}
