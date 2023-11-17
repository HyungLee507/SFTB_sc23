package CDProject.vfmarket.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserSignUpDto {

    private String email;

    private String password;

    private Integer showSize;

    private String name;

    private String age;

}
