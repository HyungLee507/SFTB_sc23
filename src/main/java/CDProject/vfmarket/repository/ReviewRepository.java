package CDProject.vfmarket.repository;

import CDProject.vfmarket.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findByItem_Id(Long itemId);
}
