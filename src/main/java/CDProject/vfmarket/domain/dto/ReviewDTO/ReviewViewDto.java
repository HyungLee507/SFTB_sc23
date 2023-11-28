package CDProject.vfmarket.domain.dto.ReviewDTO;

import CDProject.vfmarket.domain.dto.commentDTO.CommentDto;
import CDProject.vfmarket.domain.entity.WriteStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewViewDto {

    private String reviewId;

    private String reviewRegisUserId;

    private String reviewerName;

    private Integer starRate;

    private String title;

    private String content;

    private WriteStatus status;

    private LocalDateTime lastModifiedDate;

    private List<CommentDto> comments;

}
