package CDProject.vfmarket.domain.dto.commentDTO;

import CDProject.vfmarket.domain.entity.WriteStatus;
import jakarta.persistence.Lob;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDto {

    private Long commentId;

    private Long commentUserId;

    private String commentUserName;

    @Lob
    private String content;

    private WriteStatus writeStatus;

    private LocalDateTime lastModifiedDate;

//    @Nullable
//    private List<SubCommentDto> replies;

}
