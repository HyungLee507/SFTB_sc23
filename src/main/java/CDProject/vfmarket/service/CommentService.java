package CDProject.vfmarket.service;

import CDProject.vfmarket.domain.dto.commentDTO.CommentFormDto;
import CDProject.vfmarket.domain.dto.commentDTO.SubCommentFormDto;
import CDProject.vfmarket.domain.entity.Comment;
import CDProject.vfmarket.domain.entity.WriteStatus;
import CDProject.vfmarket.repository.CommentRepository;
import CDProject.vfmarket.repository.ReviewRepository;
import CDProject.vfmarket.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
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

    //todo : 이거 해야됨
    public void saveComment(Long userId, CommentFormDto commentFormDto) {
        Comment comment = Comment.builder()
                .description(commentFormDto.getDescription())
                .user(userRepository.findById(userId).get())
                .review(reviewRepository.findById(commentFormDto.getReviewId()).get())
                .writeStatus(WriteStatus.COMMENT_AVAILABLE)
                .build();
    }

    public void saveSubComment(Long userId, SubCommentFormDto subCommentFormDto) {
        Comment comment = Comment.builder()
                .description(subCommentFormDto.getContent())
                .user(userRepository.findById(userId).get())
                .parent(commentRepository.findById(subCommentFormDto.getCommentId()).get())
                .build();
    }

    public void deleteComment(Long commentId) {
        Optional<Comment> byId = commentRepository.findById(commentId);
        byId.get().setWriteStatus(WriteStatus.COMMENT_NOT_AVAILABLE);
        commentRepository.save(byId.get());
    }

    public void updateComment(Long commentId, String content) {
        Optional<Comment> byId = commentRepository.findById(commentId);
        byId.get().setDescription(content);
        commentRepository.save(byId.get());
    }


}
