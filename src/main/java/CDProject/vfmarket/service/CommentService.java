package CDProject.vfmarket.service;

import CDProject.vfmarket.domain.dto.commentDTO.CommentDto;
import CDProject.vfmarket.domain.dto.commentDTO.CommentFormDto;
import CDProject.vfmarket.domain.dto.commentDTO.CommentUpdateFormDto;
import CDProject.vfmarket.domain.entity.Comment;
import CDProject.vfmarket.domain.entity.Review;
import CDProject.vfmarket.domain.entity.User;
import CDProject.vfmarket.domain.entity.WriteStatus;
import CDProject.vfmarket.repository.CommentRepository;
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
public class CommentService {

    private final UserRepository userRepository;

    private final ReviewRepository reviewRepository;

    private final CommentRepository commentRepository;

    private final NotificationService notificationService;

    public void saveComment(Long userId, CommentFormDto commentFormDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        Review review = reviewRepository.findById(commentFormDto.getReviewId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Review not found with id: " + commentFormDto.getReviewId()));
        Comment comment = Comment.builder()
                .description(commentFormDto.getDescription())
                .user(user)
                .review(review)
                .writeStatus(WriteStatus.COMMENT_AVAILABLE)
                .build();
        notificationService.makeNotification(review.getItem().getSellerId(), review.getItem().getId(),
                review.getItem().getItemName() + " 결제 후기에 대한 댓글이 작성되어졌습니다.");
        commentRepository.save(comment);
    }

    //대댓글 기능 삭제
//    public void saveSubComment(Long userId, SubCommentFormDto subCommentFormDto) {
//        Comment comment = Comment.builder()
//                .description(subCommentFormDto.getContent())
//                .user(userRepository.findById(userId).get())
//                .parent(commentRepository.findById(subCommentFormDto.getCommentId()).get())
//                .build();
//    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + commentId));
        comment.setWriteStatus(WriteStatus.COMMENT_NOT_AVAILABLE);
        commentRepository.save(comment);
    }

    public void updateComment(CommentUpdateFormDto commentUpdateFormDto) {
        Comment comment = commentRepository.findById(commentUpdateFormDto.getCommentId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Comment not found with id: " + commentUpdateFormDto.getCommentId()));
        comment.setDescription(commentUpdateFormDto.getDescription());
        commentRepository.save(comment);
    }

    public List<CommentDto> getComments(Long reviewId) {
        return commentRepository.findAllCommentsInReview(reviewId);
    }
}
