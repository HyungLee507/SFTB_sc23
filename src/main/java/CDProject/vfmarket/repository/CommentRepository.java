package CDProject.vfmarket.repository;

import CDProject.vfmarket.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {


}
