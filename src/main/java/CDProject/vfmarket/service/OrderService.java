package CDProject.vfmarket.service;

import CDProject.vfmarket.domain.dto.OrderDTO.OrderViewDto;
import CDProject.vfmarket.domain.dto.OrderDTO.ProgressOrderDto;
import CDProject.vfmarket.domain.dto.OrderDTO.SoldItemViewDto;
import CDProject.vfmarket.domain.entity.Order;
import CDProject.vfmarket.domain.entity.OrderDetail;
import CDProject.vfmarket.domain.entity.OrderStatus;
import CDProject.vfmarket.exceptions.NotFoundException;
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

    //Todo
    public void updateBillingNumber(Long orderId, String billingNumber) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("해당 상를 찾을 수 없습니다."));
        OrderDetail orderDetail = order.getOrderDetail();
        log.info("sdlkfjsd = {}, {}", orderId, billingNumber);
        orderDetail.setBillingNumber(billingNumber);
        order.setStatus(OrderStatus.DEAL_DONE);
        orderDetailRepository.save(orderDetail);
        orderRepository.save(order);
    }


}
