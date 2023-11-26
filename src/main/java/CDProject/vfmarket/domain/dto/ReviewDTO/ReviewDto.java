package CDProject.vfmarket.domain.dto.ReviewDTO;

import CDProject.vfmarket.domain.dto.commentDTO.CommentDto;
import CDProject.vfmarket.domain.entity.WriteStatus;
import jakarta.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class ReviewDto {

    private Long reviewId;

    private Long reviewRegisUserId;

    private String userName;

    private String title;

    private String content;

    private WriteStatus writeStatus;

    @Nullable
    private List<CommentDto> comments;

    private LocalDateTime createdDate;

}
