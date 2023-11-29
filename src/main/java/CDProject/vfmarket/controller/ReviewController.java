package CDProject.vfmarket.controller;


import CDProject.vfmarket.domain.dto.ReviewDTO.ReviewDto;
import CDProject.vfmarket.domain.dto.ReviewDTO.ReviewFormDto;
import CDProject.vfmarket.domain.dto.ReviewDTO.ReviewUpdateFormDto;
import CDProject.vfmarket.global.jwt.TokenValueProvider;
import CDProject.vfmarket.service.ReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final TokenValueProvider tokenValueProvider;


    @GetMapping("/seller-reviews/{productId}")
    public List<ReviewDto> getReviews(@PathVariable Long productId) {
        return reviewService.getReviews(productId);
    }

    @PostMapping("/review-upload")
    public void reviewUpload(@RequestHeader("Authorization") String token, ReviewFormDto reviewFormDto) {
        Long buyerId = tokenValueProvider.extractUserId(token);
        reviewService.saveReview(buyerId, reviewFormDto);
    }

    @PutMapping("/review-update")
    public void reviewUpdate(@RequestHeader("Authorization") String token, @RequestParam Long reviewId,
                             ReviewUpdateFormDto reviewUpdateFormDto) {
//        Long userId = tokenValueProvider.extractUserId(token);
        reviewService.updateReview(reviewId, reviewUpdateFormDto);
    }

    @PutMapping("/review-delete")
    public void reviewDelete(@RequestHeader("Authorization") String token, @RequestParam Long reviewId) {
        reviewService.deleteReview(reviewId);
    }
}
