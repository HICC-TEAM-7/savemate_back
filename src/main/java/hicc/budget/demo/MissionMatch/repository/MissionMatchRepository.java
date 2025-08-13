package hicc.budget.demo.MissionMatch.repository;

//메서드 이름만으로 jpa가 알아서 쿼리를 생성해주는 규칙  !!
//서비스 로직 생성 전 .. 무슨 데이터를 어떻게 조회할건지 체크 !


import hicc.budget.demo.Mission.domain.Mission;
import hicc.budget.demo.MissionMatch.domain.MissionMatch;
import hicc.budget.demo.User.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

public interface MissionMatchRepository extends JpaRepository<MissionMatch, Long> {

    //유저 매칭 생성 ( 중복 금지 )
    boolean existsByMissionAndUser1AndUser2(Mission mission, User user1, User user2);

    //특정 미션의 매칭 목록 ( 페이지네이션 )
    Page<MissionMatch> findByMission(Mission mission, Pageable pageable);

    //내가 참여한 매칭들 ( 둘중 하나가 나 )
    Page<MissionMatch> findByUser1OrUser2(User user1, User user2, Pageable pageable);
}
