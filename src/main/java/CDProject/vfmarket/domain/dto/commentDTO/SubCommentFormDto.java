package CDProject.vfmarket.domain.dto.commentDTO;

import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class SubCommentFormDto {

    private Long commentId;

    @Lob
    private String content;
}
