package CDProject.vfmarket.service;

import CDProject.vfmarket.domain.dto.OrderDTO.OrderDetailSaveDTO;
import CDProject.vfmarket.domain.dto.OrderDTO.OrderDoneDTO;
import CDProject.vfmarket.domain.dto.OrderDTO.OrderViewDto;
import CDProject.vfmarket.domain.dto.OrderDTO.ProgressOrderDto;
import CDProject.vfmarket.domain.dto.OrderDTO.SoldItemViewDto;
import CDProject.vfmarket.domain.entity.Item;
import CDProject.vfmarket.domain.entity.ItemStatus;
import CDProject.vfmarket.domain.entity.Order;
import CDProject.vfmarket.domain.entity.OrderDetail;
import CDProject.vfmarket.domain.entity.OrderStatus;
import CDProject.vfmarket.exceptions.NotFoundException;
import CDProject.vfmarket.repository.ItemRepository;
import CDProject.vfmarket.repository.OrderDetailRepository;
import CDProject.vfmarket.repository.OrderRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final ItemRepository itemRepository;


    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public List<OrderViewDto> getOrderHistory(Long buyerId) {
        return orderRepository.orderViewData(buyerId);
    }

    public List<SoldItemViewDto> getSoldItemHistory(Long sellerId) {
        return orderRepository.soldViewData(sellerId);
    }

    public List<ProgressOrderDto> getProgressOrders(Long sellerId) {
        List<ProgressOrderDto> progressOrderDtos = new ArrayList<>();
        List<OrderViewDto> orderViewDtos = orderRepository.progressOrderData(sellerId);
        for (OrderViewDto orderViewDto : orderViewDtos) {
            Long orderId = orderViewDto.getOrderId();
            String address = orderDetailRepository.findAddressById(orderId);
            progressOrderDtos.add(new ProgressOrderDto(orderViewDto, address));
        }
        return progressOrderDtos;
    }

    public void updateBillingNumber(OrderDetailSaveDTO orderDetailSaveDTO) {
        Order order = orderRepository.findById(orderDetailSaveDTO.getOrderId())
                .orElseThrow(() -> new NotFoundException("해당 주문을 찾을 수 없습니다."));
        OrderDetail orderDetail = order.getOrderDetail();
        orderDetail.setBillingNumber(orderDetailSaveDTO.getBillingNumber());
        orderDetail.setDeliveryCompany(orderDetailSaveDTO.getDeliveryCompany());
        order.setStatus(OrderStatus.DEAL_DONE);

        Item item = itemRepository.findById(order.getItem().getId())
                .orElseThrow(() -> new NotFoundException("해당 상품을 찾을수 없습니다."));
        item.setStatus(ItemStatus.SOLD_OUT);

        itemRepository.save(item);
        orderDetailRepository.save(orderDetail);
        orderRepository.save(order);

    }

    public List<OrderDoneDTO> getDoneOrders(Long buyerId) {
        return orderRepository.orderDoneData(buyerId);

    }


}
