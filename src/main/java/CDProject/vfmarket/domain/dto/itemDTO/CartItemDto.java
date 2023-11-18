package CDProject.vfmarket.domain.dto.itemDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {

    private Long id;

    private String name;

    private Long price;

    private Integer shoeSize;

    private String category;

    private String description;

    private String image;

}
