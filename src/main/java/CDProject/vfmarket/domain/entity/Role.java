package CDProject.vfmarket.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    GUEST("ROLE_GUEST"), ADMIN("ROLE_ADMIN"), MEMBER("ROLE_MEMBER"), SIGN_OUT("ROLE_SIGN_OUT");
    private final String key;
}
