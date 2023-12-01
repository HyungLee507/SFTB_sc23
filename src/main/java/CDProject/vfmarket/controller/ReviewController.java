package CDProject.vfmarket.controller;


import static CDProject.vfmarket.global.AuthenticationUserId.getAuthenticatedUser;

import CDProject.vfmarket.domain.dto.ReviewDTO.ReviewDto;
import CDProject.vfmarket.domain.dto.ReviewDTO.ReviewFormDto;
import CDProject.vfmarket.domain.dto.ReviewDTO.ReviewUpdateFormDto;
import CDProject.vfmarket.service.NotificationService;
import CDProject.vfmarket.service.ReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ReviewController {

    private final ReviewService reviewService;
    private final NotificationService notificationService;


    @GetMapping("/seller-reviews/{productId}")
    public List<ReviewDto> getReviews(@PathVariable Long productId) {
        return reviewService.getReviews(productId);
    }

    @PostMapping("/review-upload")
    public void reviewUpload(@RequestHeader("Authorization") String token, @RequestBody ReviewFormDto reviewFormDto) {
        try {
            Long buyerId = getAuthenticatedUser();
            reviewService.saveReview(buyerId, reviewFormDto);
            notificationService.generateReviewNotification(reviewFormDto.getItemId(), "상품 리뷰가 작성되었습니다.");
            log.info("save success");
        } catch (RuntimeException e) {
            log.info("Review and Notification error ", e);
        }
    }

    @PutMapping("/review-update")
    public void reviewUpdate(@RequestParam Long reviewId,
                             ReviewUpdateFormDto reviewUpdateFormDto) {
        reviewService.updateReview(reviewId, reviewUpdateFormDto);
    }

    @PutMapping("review-delete")
    public void reviewDelete(@RequestHeader("Authorization") String token, @RequestParam Long reviewId) {
        reviewService.deleteReview(reviewId);
    }
}
