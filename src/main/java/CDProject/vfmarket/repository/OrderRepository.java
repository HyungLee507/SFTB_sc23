package CDProject.vfmarket.repository;

import CDProject.vfmarket.domain.dto.OrderDTO.OrderViewDto;
import CDProject.vfmarket.domain.dto.OrderDTO.SoldItemViewDto;
import CDProject.vfmarket.domain.entity.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByItem_Id(Long id);

    List<Order> findBySellerId(Long sellerId);

    List<Order> findByBuyer_Id(Long buyerId);

    @Query("SELECT new CDProject.vfmarket.domain.dto.OrderDTO.OrderViewDto(i.itemName, o.representativeImage, i.price,o.id,i.shoeSize,o.lastModifiedDate) FROM Order o JOIN o.item i WHERE o.buyer.id = :buyerId AND o.status = CDProject.vfmarket.domain.entity.OrderStatus.COMPLETE_PAYMENT")
    List<OrderViewDto> orderViewData(@Param("buyerId") Long buyerId);

    @Query("SELECT new CDProject.vfmarket.domain.dto.OrderDTO.SoldItemViewDto(i.itemName, o.representativeImage, i.price,o.id,i.shoeSize,o.lastModifiedDate) FROM Order o JOIN o.item i WHERE o.sellerId = :sellerId AND o.status = CDProject.vfmarket.domain.entity.OrderStatus.COMPLETE_PAYMENT")
    List<SoldItemViewDto> soldViewData(@Param("sellerId") Long sellerId);


}
