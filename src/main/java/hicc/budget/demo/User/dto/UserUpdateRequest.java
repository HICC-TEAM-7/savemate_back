package hicc.budget.demo.User.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

//유저 정보 수정
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    @Size(max = 30)
    private String nickname;

    @Size(min=4, max=10)
    private String password;
    private String profile;
}
