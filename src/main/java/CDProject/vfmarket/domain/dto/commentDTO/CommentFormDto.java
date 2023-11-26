package CDProject.vfmarket.domain.dto.commentDTO;

import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class CommentFormDto {

    private Long reviewId;

    @Lob
    private String description;

}
