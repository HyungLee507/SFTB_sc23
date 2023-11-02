package CDProject.vfmarket.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationResponseDto {
    private String accessToken;
    private String refreshToken;

    private String email;
    private int sendCode;
    private String message;
}
