package hicc.budget.demo.Follow.controller;

import hicc.budget.demo.Follow.dto.FriendRequestDto;
import hicc.budget.demo.Follow.dto.UserResponseDto;
import hicc.budget.demo.Follow.service.FriendService;
import hicc.budget.demo.config.auth.LoginUser;
import hicc.budget.demo.config.auth.dto.SessionUser;
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
            @LoginUser SessionUser sessionUser) {
        friendService.sendFriendRequest(sessionUser.getId(), request.getReceiverId());
        return ResponseEntity.ok().build();
    }

    // 받은 친구 요청 목록 조회
    @GetMapping("/requests")
    public ResponseEntity<List<FriendRequestDto.RequestResponse>> getReceivedRequests(
            @LoginUser SessionUser sessionUser) {
        List<FriendRequestDto.RequestResponse> requests = friendService.getReceivedRequests(sessionUser.getId());
        return ResponseEntity.ok(requests);
    }

    // 친구 요청 수락
    @PostMapping("/accept")
    public ResponseEntity<Void> acceptFriendRequest(
            @RequestBody FriendRequestDto.AcceptRequest request,
            @LoginUser SessionUser sessionUser) {
        friendService.acceptFriendRequest(request.getRequestId(), sessionUser.getId());
        return ResponseEntity.ok().build();
    }
    
    // 친구 요청 거절
    @PostMapping("/reject")
    public ResponseEntity<Void> rejectFriendRequest(
            @RequestBody FriendRequestDto.AcceptRequest request,
            @LoginUser SessionUser sessionUser) {
        friendService.rejectFriendRequest(request.getRequestId(), sessionUser.getId());
        return ResponseEntity.ok().build();
    }

    // 친구 목록 조회
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getFriends(
            @LoginUser SessionUser sessionUser) {
        List<UserResponseDto> friends = friendService.getFriends(sessionUser.getId());
        return ResponseEntity.ok(friends);
    }

    // 친구 삭제
    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> deleteFriend(
            @PathVariable Long friendId,
            @LoginUser SessionUser sessionUser) {
        friendService.deleteFriend(sessionUser.getId(), friendId);
        return ResponseEntity.ok().build();
    }
}
