package hicc.budget.demo.Follow.controller;

import hicc.budget.demo.Follow.dto.FriendRequestDto;
import hicc.budget.demo.Follow.dto.UserResponseDto;
import hicc.budget.demo.Follow.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/friends")
public class FriendController {
    private final FriendService friendService;

    // 친구 검색
    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> searchUsers(
            @RequestParam String keyword) {
        List<UserResponseDto> users = friendService.searchUsers(keyword);
        return ResponseEntity.ok(users);
    }

    // 친구 요청 보내기
    @PostMapping("/request")
    public ResponseEntity<Void> sendFriendRequest(
            @RequestBody FriendRequestDto.SendRequest request,
            @RequestHeader("X-USER-ID") Long userId) {                 // ★ 변경
        friendService.sendFriendRequest(userId, request.getReceiverId());
        return ResponseEntity.ok().build();
    }

    // 받은 친구 요청 목록 조회
    @GetMapping("/requests")
    public ResponseEntity<List<FriendRequestDto.RequestResponse>> getReceivedRequests(
            @RequestHeader("X-USER-ID") Long userId) {                  // ★ 변경
        return ResponseEntity.ok(friendService.getReceivedRequests(userId));
    }

    // 친구 요청 수락
    @PostMapping("/accept")
    public ResponseEntity<Void> acceptFriendRequest(
            @RequestBody FriendRequestDto.AcceptRequest request,
            @RequestHeader("X-USER-ID") Long userId) {                  // ★ 변경
        friendService.acceptFriendRequest(request.getRequestId(), userId);
        return ResponseEntity.ok().build();
    }
    
    // 친구 요청 거절
    @PostMapping("/reject")
    public ResponseEntity<Void> rejectFriendRequest(
            @RequestBody FriendRequestDto.AcceptRequest request,
            @RequestHeader("X-USER-ID") Long userId) {                  // ★ 변경
        friendService.rejectFriendRequest(request.getRequestId(), userId);
        return ResponseEntity.ok().build();
    }

    // 친구 목록 조회
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getFriends(
            @RequestHeader("X-USER-ID") Long userId) {                  // ★ 변경
        return ResponseEntity.ok(friendService.getFriends(userId));
    }

    // 친구 삭제
    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> deleteFriend(
            @PathVariable Long friendId,
            @RequestHeader("X-USER-ID") Long userId) {                  // ★ 변경
        friendService.deleteFriend(userId, friendId);
        return ResponseEntity.ok().build();
    }
}
