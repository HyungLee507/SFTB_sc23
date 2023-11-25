package CDProject.vfmarket.domain.dto.SignUpDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FirstRegistUserInfoDto {

    private String email;

    private String nickName;

    private Integer shoeSize;
}
