package CDProject.vfmarket.service;

import CDProject.vfmarket.domain.dto.NotificationDTO.NotificationViewDto;
import CDProject.vfmarket.domain.entity.Item;
import CDProject.vfmarket.domain.entity.Notification;
import CDProject.vfmarket.domain.entity.User;
import CDProject.vfmarket.exceptions.NotFoundException;
import CDProject.vfmarket.repository.ItemRepository;
import CDProject.vfmarket.repository.NotificationRepository;
import CDProject.vfmarket.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;


    public List<NotificationViewDto> getNotifications(Long userId) {
        return notificationRepository.findNotificationsByUserId(userId);
    }

    public void makeNotification(Long userId, Long itemId, String message) {

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("해당 유저를 찾을 수 없습니다."));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("해당 상를 찾을 수 없습니다."));
        Notification notification = Notification.builder().
                user(user)
                .isRead(false)
                .message(message)
                .item(item)
                .build();
        notificationRepository.save(notification);
//        return notification;
    }


    public void generateReviewNotification(Long itemId, String message) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("해당 상를 찾을 수 없습니다."));
        User user = userRepository.findById(item.getSellerId())
                .orElseThrow(() -> new NotFoundException("해당 유저를 찾을 수 없습니다."));

        String realMessage = item.getItemName() + "에 대한 " + message;
        Notification notification = Notification.builder().
                user(user)
                .isRead(false)
                .message(realMessage)
                .item(item)
                .build();
        notificationRepository.save(notification);
    }

    public void generateCommentNotification(Long userId, Long commentId) {

    }


    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException("해당 comment를 찾을 수 없습니다."));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
