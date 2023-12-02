package CDProject.vfmarket.repository;

import CDProject.vfmarket.domain.dto.OrderDTO.OrderDoneDTO;
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

    @Query("SELECT new CDProject.vfmarket.domain.dto.OrderDTO.OrderViewDto(o.id,i.itemName, o.representativeImage, i.price,i.id,i.shoeSize,o.lastModifiedDate) FROM Order o JOIN o.item i WHERE o.buyer.id = :buyerId AND o.status = CDProject.vfmarket.domain.entity.OrderStatus.TRANSACTION_IN")
    List<OrderViewDto> orderViewData(@Param("buyerId") Long buyerId);

    @Query("SELECT new CDProject.vfmarket.domain.dto.OrderDTO.SoldItemViewDto(o.id,i.itemName, o.representativeImage, i.price,i.shoeSize,o.lastModifiedDate) FROM Order o JOIN o.item i WHERE o.sellerId = :sellerId AND o.status = CDProject.vfmarket.domain.entity.OrderStatus.DEAL_DONE")
    List<SoldItemViewDto> soldViewData(@Param("sellerId") Long sellerId);

    @Query("SELECT new CDProject.vfmarket.domain.dto.OrderDTO.OrderViewDto(o.id,i.itemName, o.representativeImage, i.price,i.id,i.shoeSize,o.lastModifiedDate) FROM Order o JOIN o.item i WHERE o.sellerId = :sellerId AND o.status = CDProject.vfmarket.domain.entity.OrderStatus.TRANSACTION_IN")
    List<OrderViewDto> progressOrderData(@Param("sellerId") Long sellerId);

    @Query("SELECT new CDProject.vfmarket.domain.dto.OrderDTO.OrderDoneDTO(o.id,i.itemName, o.representativeImage, i.price,i.id,i.shoeSize,o.lastModifiedDate,i.reviewSubmitted,od.deliveryCompany,od.billingNumber) "
            + "FROM Order o JOIN o.item i "
            + "JOIN o.orderDetail od"
            + " WHERE o.buyer.id = :buyerId AND o.status = CDProject.vfmarket.domain.entity.OrderStatus.DEAL_DONE")
    List<OrderDoneDTO> orderDoneData(@Param("buyerId") Long buyerId);

}
