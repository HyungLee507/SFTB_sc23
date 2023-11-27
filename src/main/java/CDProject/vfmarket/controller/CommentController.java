package CDProject.vfmarket.controller;

import CDProject.vfmarket.domain.dto.commentDTO.CommentDto;
import CDProject.vfmarket.domain.dto.commentDTO.CommentFormDto;
import CDProject.vfmarket.domain.dto.commentDTO.CommentUpdateFormDto;
import CDProject.vfmarket.global.jwt.TokenValueProvider;
import CDProject.vfmarket.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private CommentService commentService;
    private TokenValueProvider tokenValueProvider;

    @PostMapping("/comment-upload")
    public void commentUpload(@RequestHeader("Authorization") String token, CommentFormDto commentFormDto) {
        Long userId = tokenValueProvider.extractUserId(token);
        commentService.saveComment(userId, commentFormDto);
    }

    @PutMapping("/comment-update")
    public void commentUpdate(CommentUpdateFormDto commentUpdateFormDto) {
        commentService.updateComment(commentUpdateFormDto);
    }

    @PutMapping("/comment-delete")
    public void commentHide(CommentUpdateFormDto commentUpdateFormDto) {
        commentService.deleteComment(commentUpdateFormDto.getCommentId());
    }

    @GetMapping("/comments")
    public List<CommentDto> getComments(@RequestParam Long reviewId) {
        return commentService.getComments(reviewId);
    }


}
