package CDProject.vfmarket.domain.dto.itemDTO;

import CDProject.vfmarket.domain.entity.ItemStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemViewDto {

    private Long id;

    private String name;

    private Long price;

    private LocalDateTime createdDate;

    private String image;

    private ItemStatus status;

}
