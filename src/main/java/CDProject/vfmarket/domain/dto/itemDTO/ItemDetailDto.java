package CDProject.vfmarket.domain.dto.itemDTO;

import CDProject.vfmarket.domain.entity.ItemStatus;
import jakarta.persistence.Lob;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDetailDto {

    private Long id;

    private String name;

    private Long price;

    private String sellerName;

    private LocalDateTime createdDate;

    private String image;

    private int shoeSize;

    private String category;

    @Lob
    private String description;

    private List<String> images;

    private List<Long> imageIds;

    private ItemStatus status;
}
