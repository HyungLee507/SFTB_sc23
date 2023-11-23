package CDProject.vfmarket.service;

import CDProject.vfmarket.domain.dto.OrderDTO.OrderViewDto;
import CDProject.vfmarket.domain.dto.OrderDTO.SoldItemViewDto;
import CDProject.vfmarket.repository.OrderRepository;
import jakarta.transaction.Transactional;
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

    public List<OrderViewDto> getOrderHistory(Long buyerId) {
        return orderRepository.orderViewData(buyerId);
    }

    public List<SoldItemViewDto> getSoldItemHistory(Long sellerId) {
        return orderRepository.soldViewData(sellerId);
    }
}
