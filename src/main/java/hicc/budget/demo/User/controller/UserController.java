package hicc.budget.demo.User.controller;

import hicc.budget.demo.User.dto.*;
import hicc.budget.demo.User.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    //회원 생성
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @RequestBody @Validated UserRequest request
    ) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity
                .status(201)            // 201 Created
                .body(response);
    }

    //전체 회원 조회
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> list = userService.getAllUsers();
        return ResponseEntity.ok(list);
    }

    //단일 회원 세부조회
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable Long id
    ) {
        UserResponse response = userService.getUser(id);
        return ResponseEntity.ok(response);
    }

    //회원 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody @Validated UserUpdateRequest request
    ) {
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }

    /** 회원 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id
    ) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
