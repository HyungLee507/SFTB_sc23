package CDProject.vfmarket.controller;


import CDProject.vfmarket.domain.dto.NotificationDTO.NotificationViewDto;
import CDProject.vfmarket.global.jwt.TokenValueProvider;
import CDProject.vfmarket.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    private final TokenValueProvider tokenValueProvider;

    @GetMapping("/notifications")
    public List<NotificationViewDto> getNotifications(@RequestHeader("Authorization") String token) {
        Long userId = tokenValueProvider.extractUserId(token);
        return notificationService.getNotifications(userId);
    }

    @PutMapping("/read-notification/{id}")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

}
