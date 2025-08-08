
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

    //회원 생성

    public UserResponse createUser(UserRequest userRequest){
        User user= userRepository.save(userRequest.toEntity());

        return UserResponse.from(user);
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

        if (request.getNickname() != null)  user.setNickname(request.getNickname());
        if (request.getPassword() != null)  user.setPassword(request.getPassword());
        if (request.getProfile()  != null)  user.setProfile(request.getProfile());

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
