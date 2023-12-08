package CDProject.vfmarket.controller;

import CDProject.vfmarket.domain.dto.commentDTO.CommentDto;
import CDProject.vfmarket.domain.dto.commentDTO.CommentFormDto;
import CDProject.vfmarket.domain.dto.commentDTO.CommentUpdateFormDto;
import CDProject.vfmarket.global.jwt.TokenValueProvider;
import CDProject.vfmarket.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final TokenValueProvider tokenValueProvider;


    // 댓글 관련 프론트와 연결
    @PostMapping("/comment-upload")
    @PreAuthorize("isAuthenticated()")
    public void commentUpload(@RequestHeader("Authorization") String token, CommentFormDto commentFormDto) {
        Long userId = tokenValueProvider.extractUserId(token);
        commentService.saveComment(userId, commentFormDto);
    }

    @PutMapping("/comment-update")
    @PreAuthorize("isAuthenticated()")
    public void commentUpdate(CommentUpdateFormDto commentUpdateFormDto) {
        commentService.updateComment(commentUpdateFormDto);
    }

    @PutMapping("/comment-delete")
    @PreAuthorize("isAuthenticated()")
    public void commentHide(CommentUpdateFormDto commentUpdateFormDto) {
        commentService.deleteComment(commentUpdateFormDto.getCommentId());
    }

    @GetMapping("/comments")
    public List<CommentDto> getComments(@RequestParam Long reviewId) {
        return commentService.getComments(reviewId);
    }


}
