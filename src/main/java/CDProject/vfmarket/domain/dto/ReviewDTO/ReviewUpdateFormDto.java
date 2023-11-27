package CDProject.vfmarket.domain.dto.ReviewDTO;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewUpdateFormDto {

    private String reviewName;

    private Integer starRate;

    @Lob
    private String content;
    
}
