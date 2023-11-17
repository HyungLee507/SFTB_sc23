package CDProject.vfmarket.domain.dto.itemDTO;

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

    private String name;

    private Long price;

    private String sellerName;

    private LocalDateTime createdDate;

    private String image;

    private int showSize;

    private String category;

    @Lob
    private String description;

    private List<String> images;
}
