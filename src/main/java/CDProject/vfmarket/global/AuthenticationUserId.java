package CDProject.vfmarket.global;

import CDProject.vfmarket.domain.entity.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUserId {

    public static Long getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((CustomUserDetails) authentication.getPrincipal()).getUserId();
    }
}
