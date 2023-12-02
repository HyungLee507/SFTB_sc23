package CDProject.vfmarket.domain.dto.OrderDTO;

import lombok.Data;

@Data
public class OrderDetailSaveDTO {

    private Long orderId;

    private String billingNumber;

    private String deliveryCompany;
}
