package CDProject.vfmarket.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

//        UserVo vo = new UserVo();
//        vo.setEmail(email);
//        vo.setPassword(password);
//
//        UserVo authUser = userService.getUser(vo);
//
//        if(authUser == null) {
//            request.setAttribute("result", "fail");
//            // 이 클래스는 Application 영역이 아니므로 서블릿 짜듯이 코드 짜야 한다.
//            request.getRequestDispatcher("/WEB-INF/views/user/login.jsp").forward(request, response);
//            return false;
//        }
//        /* 로그인 처리 */
//        HttpSession session = request.getSession(true);
//        session.setAttribute("authUser", authUser);
//        response.sendRedirect(request.getContextPath());
        return false;
    }
}
