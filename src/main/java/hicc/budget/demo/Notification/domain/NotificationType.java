package hicc.budget.demo.Notification.domain;

public enum NotificationType {
    FRIEND_REQUEST_RECEIVED,    // When someone sends a friend request
    FRIEND_REQUEST_ACCEPTED,    // When a friend request is accepted
    MISSION_INVITATION,         // When invited to a mission
    MISSION_PROGRESS,           // When a friend makes progress on a shared mission
    MISSION_COMPLETED,          // When a mission is completed
    EXPENSE_ANALYSIS,           // Weekly/Monthly expense analysis
    FRIEND_COMPARISON,          // When friend comparison data is available
    SYSTEM_ANNOUNCEMENT         // For system-wide announcements
}
