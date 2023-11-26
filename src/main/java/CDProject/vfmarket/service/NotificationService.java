package CDProject.vfmarket.service;

import CDProject.vfmarket.domain.entity.Notification;
import CDProject.vfmarket.exceptions.NotFoundException;
import CDProject.vfmarket.repository.NotificationRepository;
import CDProject.vfmarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;

    public void makeNotification(Long userId, String message) {
        Notification notification = Notification.builder().
                user(userRepository.findById(userId).get())
                .isRead(false)
                .message(message)
                .build();
        notificationRepository.save(notification);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException("해당 comment를 찾을 수 없습니다."));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
