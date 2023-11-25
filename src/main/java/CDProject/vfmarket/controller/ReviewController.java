package CDProject.vfmarket.controller;


import CDProject.vfmarket.domain.dto.ReviewDTO.ReviewFormDto;
import CDProject.vfmarket.global.jwt.TokenValueProvider;
import CDProject.vfmarket.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("review-upload")
    public void reviewUpload(@RequestHeader("Authorization") String token, ReviewFormDto reviewFormDto)
            throws NoSuchFieldException {
        Long userId = tokenValueProvider.extractUserId(token);
        reviewService.saveReview(userId, reviewFormDto);
    }

    @PutMapping("review-update")
    public void reviewUpdate(@RequestHeader("Authorization") String token, @RequestParam Long reviewId) {
        Long userId = tokenValueProvider.extractUserId(token);

    }

    @PutMapping("review-delete")
    public void reviewDelete(@RequestHeader("Authorization") String token, @RequestParam Long reviewId) {
        reviewService.deleteReview(reviewId);
    }
}
