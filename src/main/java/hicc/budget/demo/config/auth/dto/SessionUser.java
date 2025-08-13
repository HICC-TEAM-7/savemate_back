package hicc.budget.demo.config.auth.dto;

import hicc.budget.demo.User.domain.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String nickname;
    private String email;
    private Long id;

    public SessionUser(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
    }
}
