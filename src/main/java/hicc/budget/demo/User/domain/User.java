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

    //private : 필드는 감추고, getter,setter로 접근해서 직접 수정 못하게함
    //캡슐화를 지키기.
    //변수명은 소문자로 시작 .
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //db가 id 자동으로 증가시킴
    private Long id;

    private String nickname;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false,unique=true)//null 불가, 중복 불가
    private String email;

    @Column(nullable=true)
    private String profile;//이미지 url 넣기 ..


}
