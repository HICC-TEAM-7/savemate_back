package hicc.budget.demo.User.dto;
import hicc.budget.demo.User.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserRequest {

    @NotBlank(message = "이름은 필수입니다.")
    private String nickname;

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
    private String profile;

    public User toEntity()
    {
        return User.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .profile(profile).build();
    }
}
