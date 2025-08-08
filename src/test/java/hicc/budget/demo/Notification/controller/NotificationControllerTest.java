package hicc.budget.demo.Notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hicc.budget.demo.Notification.domain.NotificationType;
import hicc.budget.demo.Notification.dto.NotificationResponse;
import hicc.budget.demo.Notification.service.NotificationService;
import hicc.budget.demo.User.domain.User;
import hicc.budget.demo.config.TestConfig;
import hicc.budget.demo.config.auth.LoginUserArgumentResolver;
import hicc.budget.demo.config.auth.dto.SessionUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@Import(TestConfig.class)
class NotificationControllerTest {
    @Mock
    private NotificationService notificationService;
    
    @InjectMocks
    private NotificationController notificationController;
    
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private User testUser;
    private SessionUser testSessionUser;
    
    @BeforeEach
    void setUp() {
        LoginUserArgumentResolver loginUserArgumentResolver = new LoginUserArgumentResolver();
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController)
                .setCustomArgumentResolvers(loginUserArgumentResolver)
                .build();
        objectMapper = new ObjectMapper();
testUser = User.builder()
                .id(1L)
                .username("tester")
                .email("test@example.com")
                .name("Test User")
                .picture("test-picture-url")
                .build();
        testSessionUser = new SessionUser(testUser);
    }
    
    @Test
    void getNotifications_ReturnsNotifications() throws Exception {
        // Given
        NotificationResponse response1 = createTestNotificationResponse(1L, "Test Notification 1");
        NotificationResponse response2 = createTestNotificationResponse(2L, "Test Notification 2");
        
        when(notificationService.getUserNotifications(anyLong()))
                .thenReturn(Arrays.asList(response1, response2));
        
        // When & Then
        mockMvc.perform(get("/api/notifications")
                        .requestAttr("user", testSessionUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }
    
    @Test
    void getUnreadNotifications_ReturnsUnreadNotifications() throws Exception {
        // Given
        NotificationResponse response = createTestNotificationResponse(1L, "Unread Notification");
        
        when(notificationService.getUnreadNotifications(anyLong()))
                .thenReturn(Arrays.asList(response));
        
        // When & Then
        mockMvc.perform(get("/api/notifications/unread")
                        .requestAttr("user", testSessionUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }
    
    @Test
    void getUnreadCount_ReturnsCount() throws Exception {
        // Given
        when(notificationService.getUnreadCount(anyLong())).thenReturn(3);
        
        // When & Then
        mockMvc.perform(get("/api/notifications/unread/count")
                        .requestAttr("user", testSessionUser))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }
    
    @Test
    void markAsRead_UpdatesNotification() throws Exception {
        // Given
        doNothing().when(notificationService).markAsRead(anyLong(), anyLong());
        
        // When & Then
        mockMvc.perform(post("/api/notifications/1/read"))
                .andExpect(status().isOk());
        
        verify(notificationService, times(1)).markAsRead(1L, 1L);
    }
    
    @Test
    void markAllAsRead_UpdatesAllNotifications() throws Exception {
        // Given
        doNothing().when(notificationService).markAllAsRead(anyLong());
        
        // When & Then
        mockMvc.perform(post("/api/notifications/read-all"))
                .andExpect(status().isOk());
        
        verify(notificationService, times(1)).markAllAsRead(1L);
    }
    
    @Test
    void getNotifications_EmptyList() throws Exception {
        // Given
        when(notificationService.getUserNotifications(anyLong()))
                .thenReturn(Collections.emptyList());
        
        // When & Then
        mockMvc.perform(get("/api/notifications")
                        .requestAttr("user", testSessionUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
    
    private NotificationResponse createTestNotificationResponse(Long id, String title) {
        return NotificationResponse.builder()
                .id(id)
                .title(title)
                .message("Test message")
                .type(NotificationType.SYSTEM_ANNOUNCEMENT)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
