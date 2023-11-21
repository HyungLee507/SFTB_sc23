package CDProject.vfmarket.global.oauth2.handler;

import CDProject.vfmarket.domain.entity.Role;
import CDProject.vfmarket.domain.entity.User;
import CDProject.vfmarket.global.jwt.Service.JwtService;
import CDProject.vfmarket.global.oauth2.CustomOAuth2User;
import CDProject.vfmarket.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("로그인 성공!!");
//        response.sendRedirect("/hello.html");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
            if (oAuth2User.getRole() == Role.ADMIN) {
                Optional<User> findUser = userRepository.findByEmail(oAuth2User.getEmail());
                String accessToken = jwtService.createAccessToken(findUser.get().getName(), findUser.get().getId(),
                        findUser.get().getEmail(),
                        findUser.get()
                                .getRole());
                response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
                response.sendRedirect("/user/input");
//                response.sendRedirect("http://localhost:8080/product/list"); // 프론트의 회원가입 추가 정보 입력 폼으로 리다이렉트

                jwtService.sendAccessAndRefreshToken(response, accessToken, null);
//                User findUser = userRepository.findByEmail(oAuth2User.getEmail())
//                                .orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 유저가 없습니다."));
//                findUser.authorizeUser();
            } else {
                loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성
            }
        } catch (Exception e) {
            throw e;
        }

    }

    //  소셜 로그인 시에도 무조건 토큰 생성하지 말고 JWT 인증 필터처럼 RefreshToken 유/무에 따라 다르게 처리해보기
    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        Optional<User> user = userRepository.findByEmail(oAuth2User.getEmail());
        String accessToken = jwtService.createAccessToken(user.get().getName(), user.get().getId(),
                user.get().getEmail(),
                user.get().getRole());
        String refreshToken = jwtService.createRefreshToken(user.get().getId(), user.get().getEmail(),
                user.get().getRole());
        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
        response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);
        response.sendRedirect("/product/list");

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
    }

}
