package hicc.budget.demo.Notification.repository;

import hicc.budget.demo.Notification.domain.Notification;
import hicc.budget.demo.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);
    int countByUserIdAndIsReadFalse(Long userId);
    List<Notification> findByUserAndIsReadFalseOrderByCreatedAtDesc(User user);
}
