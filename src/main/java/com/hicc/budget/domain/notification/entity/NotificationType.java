package com.hicc.budget.domain.notification.entity;

public enum NotificationType {
    MISSION_COMPLETED,  // 미션 완료
    MISSION_DEADLINE,   // 미션 마감 임박
    FRIEND_REQUEST,     // 친구 요청
    FRIEND_ACCEPTED,    // 친구 수락
    EXPENSE_ALERT,      // 지출 알림
    BUDGET_WARNING,     // 예산 초과 경고
    SYSTEM,             // 시스템 공지
    ETC                 // 기타 알림
}
