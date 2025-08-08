package hicc.budget.demo.User.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column
    private String picture;

    @Column
    private String nickname;

    @Column
    private String profile; // 프로필 이미지 URL

    // 편의 메서드 추가
    public String getPicture() {
        return picture != null ? picture : "";
    }

    public String getNickname() {
        return nickname != null ? nickname : name;
    }

    public String getProfile() {
        return profile != null ? profile : "";
    }
}
