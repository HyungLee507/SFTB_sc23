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
import CDProject.vfmarket.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    private final NotificationService notificationService;

    public void saveOrder(Long userId, OrderSaveDto orderSaveDto) {

        Item items = itemRepository.findById(orderSaveDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + orderSaveDto.getId()));
        items.setStatus(ItemStatus.TRANSACTION_IN);

        User sellerUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        Image firstImage = getFirstImage(items.getId());

        String fileName = firstImage.getFileName();
        Order order = Order.builder()
                .paymentPrice(orderSaveDto.getPaid_amount())
                .sellerName(orderSaveDto.getSeller_name())
                .sellerId(items.getSellerId())
                .merchant_uid(orderSaveDto.getMerchant_uid())
                .status(OrderStatus.TRANSACTION_IN)
                .representativeImage(fileName)
                .build();
        if (userRepository.findById(userId).isPresent()) {
            order.setBuyer(userRepository.findById(userId).get());
        }
        order.setItem(items);

//        orderRepository.save(order);
        itemRepository.save(items);

//        Order savedOrder = orderRepository.findByItem_Id(orderSaveDto.getId());
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
        orderDetail.setOrder(order);
        orderRepository.save(order);

        orderDetailRepository.save(orderDetail);
        notificationService.makeNotification(userId, orderSaveDto.getId(),
                orderSaveDto.getName() + " 상품 결제를 하셨습니다.");
        notificationService.makeNotificationBySellerName(orderSaveDto.getSeller_name(), orderSaveDto.getId(),
                orderSaveDto.getName() + " 상품에 대한 거래가 체결되었습니다.");
    }

    public Image getFirstImage(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Item not found"));
        return item.getImages().isEmpty() ? null : item.getImages().get(0);
    }

}
