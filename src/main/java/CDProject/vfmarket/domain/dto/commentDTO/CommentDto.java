package CDProject.vfmarket.domain.dto.commentDTO;

import CDProject.vfmarket.domain.entity.WriteStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.Lob;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class CommentDto {

    private Long commentId;

    private Long commentUserId;

    private String commentUserName;

    @Lob
    private String content;

    private WriteStatus writeStatus;

    @Nullable
    private List<SubCommentDto> replies;

    private LocalDateTime createdDate;

}
