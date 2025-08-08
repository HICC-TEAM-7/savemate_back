package hicc.budget.demo.Notification.service;

import hicc.budget.demo.Mission.domain.Mission;
import hicc.budget.demo.Notification.domain.Notification;
import hicc.budget.demo.Notification.domain.NotificationType;
import hicc.budget.demo.Notification.repository.NotificationRepository;
import hicc.budget.demo.User.domain.User;
import hicc.budget.demo.User.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class NotificationServiceTest {
    @Mock
    private NotificationRepository notificationRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private NotificationService notificationService;
    
    private User testUser;
    private User testSender;
    private Mission testMission;
    
    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .name("Test User")
                .email("test@example.com")
                .build();
                
        testSender = User.builder()
                .id(2L)
                .username("sender")
                .name("Test Sender")
                .email("sender@example.com")
                .build();
                
        testMission = Mission.builder()
                .id(1L)
                .title("Test Mission")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(30))
                .isActive(true)
                .build();
    }
    
    @Test
    void createFriendRequestNotification_Success() {
        // Given
        when(notificationRepository.save(any(Notification.class))).thenAnswer(i -> i.getArguments()[0]);
        
        // When
        Notification notification = notificationService.createFriendRequestNotification(testUser, testSender);
        
        // Then
        assertNotNull(notification);
        assertEquals(testUser, notification.getUser());
        assertEquals(testSender, notification.getSender());
        assertEquals(NotificationType.FRIEND_REQUEST_RECEIVED, notification.getType());
        assertTrue(notification.getMessage().contains(testSender.getUsername()));
    }
    
    @Test
    void getUnreadCount_ReturnsCorrectCount() {
        // Given
        when(notificationRepository.countByUserIdAndIsReadFalse(testUser.getId())).thenReturn(3L);
        
        // When
        long count = notificationService.getUnreadCount(testUser.getId());
        
        // Then
        assertEquals(3L, count);
    }
    
    @Test
    void markAsRead_UpdatesNotificationStatus() {
        // Given
        Notification notification = Notification.builder()
                .id(1L)
                .user(testUser)
                .title("Test Notification")
                .message("This is a test notification")
                .type(NotificationType.SYSTEM_ANNOUNCEMENT)
                .isRead(false)
                .build();
                
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(i -> i.getArguments()[0]);
        
        // When
        notificationService.markAsRead(1L, testUser.getId());
        
        // Then
        assertTrue(notification.isRead());
    }
    
    @Test
    void markAllAsRead_UpdatesAllNotifications() {
        // Given
        Notification n1 = Notification.builder().id(1L).user(testUser).isRead(false).build();
        Notification n2 = Notification.builder().id(2L).user(testUser).isRead(false).build();
        
        when(notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(testUser.getId()))
                .thenReturn(Arrays.asList(n1, n2));
                
        when(notificationRepository.saveAll(anyList())).thenAnswer(i -> i.getArguments()[0]);
        
        // When
        notificationService.markAllAsRead(testUser.getId());
        
        // Then
        assertTrue(n1.isRead());
        assertTrue(n2.isRead());
        verify(notificationRepository, times(1)).saveAll(anyList());
    }
}
