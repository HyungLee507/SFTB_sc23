package CDProject.vfmarket.domain.dto.ReviewDTO;

import CDProject.vfmarket.domain.dto.commentDTO.CommentDto;
import CDProject.vfmarket.domain.entity.WriteStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ReviewDto {

    private Long reviewId;

    private Long reviewRegisUserId;

    private String reviewerName;

    private Integer starRate;

    private String title;

    private String content;

    private WriteStatus status;

    private LocalDateTime lastModifiedDate;

    private List<CommentDto> comments = new ArrayList<>();

}
