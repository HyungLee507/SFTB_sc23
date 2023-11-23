package CDProject.vfmarket.domain.dto.itemDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SalesItemDto {

    private Long itemId;

    private Long price;

    private String itemName;

    private String imageName;
}
