package CDProject.vfmarket.controller;

import CDProject.vfmarket.domain.dto.commentDTO.CommentFormDto;
import CDProject.vfmarket.global.jwt.TokenValueProvider;
import CDProject.vfmarket.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private CommentService commentService;
    private TokenValueProvider tokenValueProvider;

    @PostMapping("comment-upload")
    public void reviewUpload(@RequestHeader("Authorization") String token, CommentFormDto commentFormDto)
            throws NoSuchFieldException {
        Long userId = tokenValueProvider.extractUserId(token);
        commentService.saveComment(userId, commentFormDto);
    }

}
