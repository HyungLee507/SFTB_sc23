package CDProject.vfmarket.repository;

import CDProject.vfmarket.domain.entity.StyleShot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StyleShotRepository extends JpaRepository<StyleShot, Long> {

    List<StyleShot> findByUser_Id(Long userId);
}
