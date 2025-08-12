package hicc.budget.demo.Follow.repository;

import hicc.budget.demo.Follow.domain.FriendRequest;
import hicc.budget.demo.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByReceiverAndStatus(User receiver, hicc.budget.demo.Follow.domain.FriendRequestStatus status);
    List<FriendRequest> findBySender(User sender);
    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);
    boolean existsBySenderAndReceiver(User sender, User receiver);
}
