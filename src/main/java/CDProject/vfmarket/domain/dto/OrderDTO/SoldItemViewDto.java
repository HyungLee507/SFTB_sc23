package CDProject.vfmarket.domain.dto.OrderDTO;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoldItemViewDto {

    private Long orderId;

    private String name;

    private String image;

    private Long price;

    private Integer shoeSize;

    private LocalDateTime createdDate;


}
