package CDProject.vfmarket.service;

import CDProject.vfmarket.domain.dto.ReviewDTO.ReviewDto;
import CDProject.vfmarket.domain.dto.ReviewDTO.ReviewFormDto;
import CDProject.vfmarket.domain.dto.ReviewDTO.ReviewUpdateFormDto;
import CDProject.vfmarket.domain.dto.commentDTO.CommentDto;
import CDProject.vfmarket.domain.entity.Item;
import CDProject.vfmarket.domain.entity.Review;
import CDProject.vfmarket.domain.entity.User;
import CDProject.vfmarket.domain.entity.WriteStatus;
import CDProject.vfmarket.repository.CommentRepository;
import CDProject.vfmarket.repository.ItemRepository;
import CDProject.vfmarket.repository.ReviewRepository;
import CDProject.vfmarket.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    private final ItemRepository itemRepository;

    public List<ReviewDto> getReviews(Long itemId) {
        Long sellerId = itemRepository.findSellerIdByItemId(itemId);
        List<ReviewDto> reviews = reviewRepository.findALlReviewBySellerId(sellerId);
        for (ReviewDto reviewDto : reviews) {
            List<CommentDto> comments = commentRepository.findAllCommentsInReview(reviewDto.getReviewId());
            reviewDto.setComments(comments);
        }
        return reviews;
    }

    public void saveReview(Long reviewerId, ReviewFormDto reviewFormDto) {
        Item findItem = itemRepository.findById(reviewFormDto.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + reviewFormDto.getItemId()));
        User seller = userRepository.findById(findItem.getSellerId())
                .orElseThrow(() -> new EntityNotFoundException("seller not found with id: " + findItem.getSellerId()));
        User buyer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new EntityNotFoundException("buyer not found with id: " + reviewerId));
        Review uploadReview = Review.builder()
                .seller(seller)
                .buyer(buyer)
                .item(findItem)
                .starRate(reviewFormDto.getStarRate())
                .writeStatus(WriteStatus.REVIEW_AVAILABLE)
                .title(reviewFormDto.getTitle())
                .content(reviewFormDto.getContent())
                .build();
        reviewRepository.save(uploadReview);

//        List<String> imageList = new ArrayList<>();
//        for (MultipartFile uploadFile : reviewFormDto.getImages()) {
//            try {
//                String uploadFileName = uploadFile.getOriginalFilename();
//                String uuid = UUID.randomUUID().toString();
//                uploadFileName = uuid + "_" + uploadFileName;
//                String uploadFolder = "/Users/leedonghyun/Desktop/images";
//                File saveFile = new File(uploadFolder, uploadFileName);
//                log.info("uploadFileName is {}", uploadFileName);
//                uploadFile.transferTo(saveFile);
//                imageList.add(uploadFileName);
//                log.info("imageList is {}", imageList);
//            } catch (Exception e) {
//                log.info("file error is {}", e.getMessage());
//                throw new NoSuchFieldException("해당 파일을 찾을 수 없습니다.");
//            }
//        }
//        log.info("imageList size is {}", imageList.size());
//        try {
//            log.info("imageList size is {}", imageList.size());
//            Optional<User> seller = userRepository.findById(userId);
//            String sellerName = seller.get().getName();

//            Review uploadReview = Review.builder()
//                    .user(seller.get())
//                    .item(findItem.get())
//                    .starRate(reviewFormDto.getStarRate())
//                    .writeStatus(WriteStatus.REVIEW_AVAILABLE)
//                    .reviewName(reviewFormDto.getName())
//                    .content(reviewFormDto.getDescription())
//                    .build();
//            for (String fileName : imageList) {
//                List<ReviewImage> images = uploadReview.getImages();
//                images.add(new ReviewImage(fileName, uploadReview));
//            }
//            log.info("item data is {}", uploadReview.getItem());
//            reviewRepository.save(uploadReview);
//        } catch (Exception e) {
//            log.info("token error is {}", e.getMessage());
//        }
    }

    public void deleteReview(Long reviewId) {
        Review byItemId = reviewRepository.findByItem_Id(reviewId);
        byItemId.setWriteStatus(WriteStatus.REVIEW_NOT_AVAILABLE);
        reviewRepository.save(byItemId);
    }

    public void updateReview(Long reviewId, ReviewUpdateFormDto reviewUpdateFormDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewId));
        review.setTitle(reviewUpdateFormDto.getReviewName());
        review.setStarRate(reviewUpdateFormDto.getStarRate());
        review.setContent(reviewUpdateFormDto.getContent());
        reviewRepository.save(review);
    }


}
