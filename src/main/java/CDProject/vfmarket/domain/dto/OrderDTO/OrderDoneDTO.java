package CDProject.vfmarket.domain.dto.OrderDTO;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDoneDTO {

    private Long orderId;

    //상품 이름
    private String name;

    private String image;

    private Long price;

    private Long id;

    private Integer shoeSize;

    private LocalDateTime createdDate;

    private boolean reviewSubmitted;

    private String deliveryCompany;

    private String billingNumber;
}
