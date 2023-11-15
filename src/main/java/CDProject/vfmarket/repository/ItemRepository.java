package CDProject.vfmarket.repository;

import CDProject.vfmarket.domain.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
