package CDProject.vfmarket.payment;

import CDProject.vfmarket.domain.dto.OrderDTO.OrderSaveDto;
import CDProject.vfmarket.domain.entity.Image;
import CDProject.vfmarket.domain.entity.Item;
import CDProject.vfmarket.domain.entity.ItemStatus;
import CDProject.vfmarket.domain.entity.Order;
import CDProject.vfmarket.domain.entity.OrderDetail;
import CDProject.vfmarket.domain.entity.OrderStatus;
import CDProject.vfmarket.domain.entity.User;
import CDProject.vfmarket.repository.ItemRepository;
import CDProject.vfmarket.repository.OrderDetailRepository;
import CDProject.vfmarket.repository.OrderRepository;
import CDProject.vfmarket.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class PaymentService {
    private final OrderDetailRepository orderDetailRepository;


    private final OrderRepository orderRepository;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    public void saveOrder(Long userId, OrderSaveDto orderSaveDto) {

        Optional<Item> items = itemRepository.findById(orderSaveDto.getId());
//        if(items.isPresent()){
//            Item item = items.get();
//        }
        log.info("item sellerName is {}", items.get().getSellerName());
        items.get().setStatus(ItemStatus.STOP_SELLING);
        Optional<User> sellerUser = userRepository.findById(userId);
        log.info("sellerUser name is {}", sellerUser.get().getName());
        Image firstImage = getFirstImage(items.get().getId());

        String fileName = firstImage.getFileName();
        log.info("findImage name is {}", fileName);
        try {
            Order order = Order.builder()
                    .paymentPrice(orderSaveDto.getPaid_amount())
                    .sellerName(orderSaveDto.getSeller_name())
                    .sellerId(items.get().getSellerId())
                    .merchant_uid(orderSaveDto.getMerchant_uid())
                    .status(OrderStatus.COMPLETE_PAYMENT)
                    .representativeImage(fileName)
                    .build();
            if (userRepository.findById(userId).isPresent()) {
                order.setBuyer(userRepository.findById(userId).get());
            }
            order.setItem(items.get());
            orderRepository.save(order);
            itemRepository.save(items.get());
        } catch (Exception e) {
            log.info("order error!!", e);
        }
        Order savedOrder = orderRepository.findByItem_Id(orderSaveDto.getId());
        OrderDetail orderDetail = OrderDetail.builder()
                .apply_num(orderSaveDto.getApply_num())
                .buyer_addr(orderSaveDto.getBuyer_addr())
                .buyer_email(orderSaveDto.getBuyer_email())
                .buyer_name(orderSaveDto.getBuyer_name())
                .buyer_postcode(orderSaveDto.getBuyer_postcode())
                .card_name(orderSaveDto.getCard_name())
                .card_number(orderSaveDto.getCard_number())
                .pg_provider(orderSaveDto.getPg_provider())
                .imp_uid(orderSaveDto.getImp_uid())
                .build();
        orderDetail.setOrder(savedOrder);
        orderDetailRepository.save(orderDetail);

//        orderRepository.insertOrder(orders);

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
    }

    public Image getFirstImage(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Item not found"));
        return item.getImages().isEmpty() ? null : item.getImages().get(0);
    }

}
