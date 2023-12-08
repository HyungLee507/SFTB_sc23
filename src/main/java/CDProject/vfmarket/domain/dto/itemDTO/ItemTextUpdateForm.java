package CDProject.vfmarket.domain.dto.itemDTO;

import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class ItemTextUpdateForm {

    private Long itemId;
    private String name;
    private Integer shoeSize;
    private Long price;
    private String category;
    @Lob
    private String description;
}
