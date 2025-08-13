package hicc.budget.demo.User.repository;

import hicc.budget.demo.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 닉네임으로 유저 존재 여부 확인
     */
    boolean existsByNickname(String nickname);
    
    /**
     * 이메일로 유저 존재 여부 확인
     */
    boolean existsByEmail(String email);
    
    /**
     * 유저 ID로 유저 조회
     */
    @Override
    @NonNull
    Optional<User> findById(@NonNull Long id);
    
    /**
     * 닉네임이나 이메일로 유저 검색 (친구 검색용)
     */
    List<User> findByNicknameContainingOrEmailContaining(String nickname, String email);
    
    /**
     * 닉네임으로 유저 조회
     */
    Optional<User> findByNickname(String nickname);
    
    /**
     * 이메일로 유저 조회
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 유저 ID로 존재 여부 확인
     */
    boolean existsById(@NonNull Long id);
    
    /**
     * 여러 ID로 유저 목록 조회
     */
    List<User> findAllByIdIn(Set<Long> ids);
}
