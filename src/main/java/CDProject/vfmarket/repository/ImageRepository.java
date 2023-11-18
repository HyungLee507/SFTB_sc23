package CDProject.vfmarket.repository;

import CDProject.vfmarket.domain.entity.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findImagesByItemId(Long itemId);
}
