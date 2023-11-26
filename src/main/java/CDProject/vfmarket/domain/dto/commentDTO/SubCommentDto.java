package CDProject.vfmarket.domain.dto.commentDTO;

import CDProject.vfmarket.domain.entity.WriteStatus;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SubCommentDto {

    private Long commentId;

    private Long commentUserId;

    private String commentUserName;

    private String content;

    private WriteStatus writeStatus;

    private LocalDateTime createdDate;
}
