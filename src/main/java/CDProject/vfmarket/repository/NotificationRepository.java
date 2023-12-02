package CDProject.vfmarket.repository;


import CDProject.vfmarket.domain.dto.NotificationDTO.NotificationViewDto;
import CDProject.vfmarket.domain.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("select new CDProject.vfmarket.domain.dto.NotificationDTO.NotificationViewDto(n.message,n.createdDate,n.isRead)"
            + " from Notification n"
            + " left join n.user u"
//            + " left join n.item i"
            + " where u.id = :userId")
    List<NotificationViewDto> findNotificationsByUserId(@Param("userId") Long userId);
}
