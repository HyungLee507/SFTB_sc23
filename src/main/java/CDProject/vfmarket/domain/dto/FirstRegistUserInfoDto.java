package CDProject.vfmarket.domain.dto;

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
