package CDProject.vfmarket.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StyleShotDto {
    Long styleShotId;
    String styleShotName;
    Long itemId;
    String itemName;
}
