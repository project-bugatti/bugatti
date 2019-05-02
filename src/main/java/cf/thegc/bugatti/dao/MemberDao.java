package cf.thegc.bugatti.dao;

import cf.thegc.bugatti.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberDao {

    Page<LimitedMember> getMembers(Pageable pageable);

    Optional<Member> getMemberById(UUID memberId);

    int updateMemberById(UUID memberId, Member member);

    Boolean toggleActive(UUID memberId);

}
