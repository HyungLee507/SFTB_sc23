package CDProject.vfmarket.repository;

import CDProject.vfmarket.domain.dto.commentDTO.CommentDto;
import CDProject.vfmarket.domain.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select new CDProject.vfmarket.domain.dto.commentDTO.CommentDto(c.id,u.id,u.name,c.description,c.writeStatus,c.lastModifiedDate)"
            + " from Comment c"
            + " left join c.user u"
            + " left join c.review r"
            + " where r.id = :reviewId")
    List<CommentDto> findAllCommentsInReview(@Param("reviewId") Long reviewId);
}
