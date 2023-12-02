package CDProject.vfmarket.domain.dto.NotificationDTO;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationViewDto {

    private String message;

    private LocalDateTime createdTime;

    private boolean isRead;

//    private Long itemId;

}
