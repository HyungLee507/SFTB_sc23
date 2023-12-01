package CDProject.vfmarket.repository;

import CDProject.vfmarket.domain.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Query("select od.billingNumber from OrderDetail od"
            + " left join od.order o"
            + " where o.id = :orderId ")
    String findBillingNumberById(@Param("orderId") Long orderId);


    @Query("select od.buyer_addr from OrderDetail od"
            + " left join od.order o"
            + " where o.id = :orderId ")
    String findAddressById(@Param("orderId") Long orderId);

}
