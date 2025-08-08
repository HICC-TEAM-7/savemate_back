package hicc.budget.demo.Follow.service;

import hicc.budget.demo.Follow.domain.FriendRequest;
import hicc.budget.demo.Follow.domain.FriendRequestStatus;
import hicc.budget.demo.Follow.domain.Friendship;
import hicc.budget.demo.Follow.dto.FriendRequestDto;
import hicc.budget.demo.Follow.dto.UserResponseDto;
import hicc.budget.demo.Follow.exception.FriendException;
import hicc.budget.demo.Follow.exception.FriendExceptionType;
import hicc.budget.demo.Follow.repository.FriendRequestRepository;
import hicc.budget.demo.Follow.repository.FriendshipRepository;
import hicc.budget.demo.Notification.service.NotificationService;
import hicc.budget.demo.User.domain.User;
import hicc.budget.demo.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final NotificationService notificationService;

    // 친구 검색
    @Transactional(readOnly = true)
    public List<UserResponseDto> searchUsers(String keyword) {
        List<User> users = userRepository.findByUsernameContainingOrNameContaining(keyword, keyword);
        return users.stream()
                .map(user -> UserResponseDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .name(user.getName())
                        .build())
                .collect(Collectors.toList());
    }

    // 친구 요청 보내기
    @Transactional
    public void sendFriendRequest(Long senderId, Long receiverId) {
        if (senderId.equals(receiverId)) {
            throw new FriendException(FriendExceptionType.CANNOT_REQUEST_TO_SELF);
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new FriendException(FriendExceptionType.USER_NOT_FOUND));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new FriendException(FriendExceptionType.USER_NOT_FOUND));

        // 이미 친구인지 확인
        if (friendshipRepository.existsByUserAndFriend(sender, receiver)) {
            throw new FriendException(FriendExceptionType.ALREADY_FRIENDS);
        }

        // 이미 요청을 보냈는지 확인
        if (friendRequestRepository.existsBySenderAndReceiver(sender, receiver)) {
            throw new FriendException(FriendExceptionType.ALREADY_REQUESTED);
        }

        FriendRequest friendRequest = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .build();

        friendRequestRepository.save(friendRequest);
        
        // Send notification to receiver
        try {
            notificationService.createFriendRequestNotification(receiver, sender);
        } catch (Exception e) {
            log.error("Failed to send friend request notification", e);
            // Don't fail the request if notification fails
        }
    }

    // 받은 친구 요청 목록 조회
    @Transactional(readOnly = true)
    public List<FriendRequestDto.RequestResponse> getReceivedRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new FriendException(FriendExceptionType.USER_NOT_FOUND));

        return friendRequestRepository.findByReceiverAndStatus(user, FriendRequestStatus.PENDING).stream()
                .map(request -> new FriendRequestDto.RequestResponse(
                        request.getId(),
                        request.getSender().getId(),
                        request.getSender().getUsername(),
                        request.getSender().getName()
                ))
                .collect(Collectors.toList());
    }

    // 친구 요청 수락
    @Transactional
    public void acceptFriendRequest(Long requestId, Long userId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new FriendException(FriendExceptionType.REQUEST_NOT_FOUND));

        if (!request.getReceiver().getId().equals(userId)) {
            throw new FriendException(FriendExceptionType.NOT_REQUEST_RECEIVER);
        }

        if (request.getStatus() != FriendRequestStatus.PENDING) {
            throw new FriendException(FriendExceptionType.INVALID_REQUEST_STATUS);
        }

        User sender = request.getSender();
        User receiver = request.getReceiver();

        // Create friendship both ways
        Friendship friendship1 = new Friendship(sender, receiver);
        Friendship friendship2 = new Friendship(receiver, sender);
        
        friendshipRepository.save(friendship1);
        friendshipRepository.save(friendship2);
        
        // Send notification to the original sender that their request was accepted
        try {
            notificationService.createFriendRequestAcceptedNotification(sender, receiver);
        } catch (Exception e) {
            log.error("Failed to send friend request accepted notification", e);
            // Don't fail the request if notification fails
        }
        
        // 요청 상태 업데이트
        request.updateStatus(FriendRequestStatus.ACCEPTED);
        friendRequestRepository.save(request);
    }

    // 친구 목록 조회
    @Transactional(readOnly = true)
    public List<UserResponseDto> getFriends(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new FriendException(FriendExceptionType.USER_NOT_FOUND));

        return friendshipRepository.findByUser(user).stream()
                .map(friendship -> UserResponseDto.builder()
                        .id(friendship.getFriend().getId())
                        .username(friendship.getFriend().getUsername())
                        .name(friendship.getFriend().getName())
                        .build())
                .collect(Collectors.toList());
    }

    // 친구 삭제
    @Transactional
    public void deleteFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new FriendException(FriendExceptionType.USER_NOT_FOUND));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new FriendException(FriendExceptionType.USER_NOT_FOUND));

        // 양방향 친구 관계 삭제
        friendshipRepository.deleteByUserAndFriend(user, friend);
        friendshipRepository.deleteByUserAndFriend(friend, user);
    }
    
    // 친구 요청 거절
    @Transactional
    public void rejectFriendRequest(Long requestId, Long userId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new FriendException(FriendExceptionType.REQUEST_NOT_FOUND));

        if (!request.getReceiver().getId().equals(userId)) {
            throw new FriendException(FriendExceptionType.NOT_REQUEST_RECEIVER);
        }

        if (request.getStatus() != FriendRequestStatus.PENDING) {
            throw new FriendException(FriendExceptionType.INVALID_REQUEST_STATUS);
        }

        // 요청 상태를 REJECTED로 업데이트
        request.updateStatus(FriendRequestStatus.REJECTED);
        friendRequestRepository.save(request);
    }
}
