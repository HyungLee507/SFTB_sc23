package CDProject.vfmarket.repository;

import CDProject.vfmarket.domain.dto.ReviewDTO.ReviewDto;
import CDProject.vfmarket.domain.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findByItem_Id(Long itemId);

    @Query("select new CDProject.vfmarket.domain.dto.ReviewDTO.ReviewDto(r.id,b.id,b.name,r.title,r.content,r.writeStatus,,r.lastModifiedDate)"
            + " from Review r"
            + " left join r.item i"
            + " left join r.comments c"
            + " left join r.seller s"
            + " left join r.buyer b"
            + " where u.id = :sellerId")
    List<ReviewDto> findALlReviewBySellerId(Long sellerId);

}
