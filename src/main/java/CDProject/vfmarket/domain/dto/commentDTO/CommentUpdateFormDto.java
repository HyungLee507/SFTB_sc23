package CDProject.vfmarket.domain.dto.commentDTO;

import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class CommentUpdateFormDto {

    private Long reviewId;

    private Long commentId;

    @Lob
    private String description;
}
