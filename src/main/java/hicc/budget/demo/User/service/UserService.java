
//기능 구현 위치
//repository 호출해서 실제 동작 정의

package hicc.budget.demo.User.service;
//도메인 객체 가져옴
import hicc.budget.demo.User.domain.User;
//dto 코드 다 가지고옴
import hicc.budget.demo.User.dto.*;
//userrepository 갖고옴
import hicc.budget.demo.User.repository.UserRepository;
//final 필드를 파라미터로 받는 생성자를 자동 생성
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
//트랜잭션 : 성공 or 실패 원칙을 지켜주는 db 작업 안전장치
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

//여러개의 데이터를 담는 list 컬렉션 사용 .
//여러명의 유저 정보 반환할때.. 각 유저정보를 userresponse dto에 담아서 반환해야.
import java.util.List;

//stream처럼 데이터를 흐름처럼 하나씩 처리하는 기술임.
//이후에 결과를 list등으로 모아야 끝나는데 ( 모으는 역할을 함 )
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //회원 생성

    public UserResponse createUser(UserRequest userRequest) {
        // 닉네임 중복 체크
        if (userRepository.existsByNickname(userRequest.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
        
        // 이메일 중복 체크
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        User user = userRequest.toEntity();
        
        // 비밀번호 해싱
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        User savedUser = userRepository.save(user);
        return UserResponse.from(savedUser);
    }

    //단건 조회
    @Transactional(readOnly = true)
    public UserResponse getUser(Long id){
        User user =userRepository.findById(id).orElseThrow(()->new RuntimeException("유저를 찾을 수 없습니다. id=" + id));
        return UserResponse.from(user);
    }

    //전체 조회
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)

                .collect(Collectors.toList());
    }
    //정보 수정
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다. id=" + id));

        // 닉네임 업데이트 요청이 있고, 기존 닉네임과 다른 경우
        if (request.getNickname() != null && !request.getNickname().equals(user.getNickname())) {
            // 새로운 닉네임이 이미 사용 중인지 확인
            if (userRepository.existsByNickname(request.getNickname())) {
                throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
            }
            user.setNickname(request.getNickname());
        }
        
        // 비밀번호 업데이트
        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // 변경 감지(dirty checking)로 자동 반영
        return UserResponse.from(user);
    }

    //회원 삭제
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("유저를 찾을 수 없습니다. id=" + id);
        }
        userRepository.deleteById(id);
    }
}
