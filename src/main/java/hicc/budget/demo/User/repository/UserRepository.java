package hicc.budget.demo.User.repository;


import hicc.budget.demo.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//JPA에서 DB랑 직접 소통하는 역할을 하는 인터페이스

public interface UserRepository extends JpaRepository<User, Long> {

    //유저를 이메일로 찾음
    Optional<User> findById(Long id);

}
