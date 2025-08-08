package hicc.budget.demo.User.dto;
import hicc.budget.demo.User.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
//사용자 정보 조회 응답용
//필요한 정보만 담기 .

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String nickname;
    private String email;
    private String profile;

    //user 엔티티 받아서 반환 !
    public static UserResponse from(User user)
    {
        return UserResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profile(user.getProfile())
                .build();
    }

}
