package CDProject.vfmarket.domain.dto.OrderDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSaveDto {
    //item_id
    private Long id;

    private String seller_name;

    private String apply_num;

    private String buyer_addr;

    private String buyer_email;

    private String buyer_name;

    private String buyer_postcode;

    private String buyer_tel;

    private String card_name;

    private String card_number;

    private String merchant_uid;

    private String name;

    private Long paid_amount;

    private String pg_provider;

    private String imp_uid;

}
