package hicc.budget.demo.Follow.repository;

import hicc.budget.demo.Follow.domain.Friendship;
import hicc.budget.demo.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByUser(User user);
    List<Friendship> findByFriend(User friend);
    Optional<Friendship> findByUserAndFriend(User user, User friend);
    boolean existsByUserAndFriend(User user, User friend);
    void deleteByUserAndFriend(User user, User friend);
}
