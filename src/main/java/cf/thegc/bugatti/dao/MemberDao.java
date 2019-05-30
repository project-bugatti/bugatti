package cf.thegc.bugatti.dao;

import cf.thegc.bugatti.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberDao {
    Member addMember(Member member);

    List<LimitedMember> getAllMembers(Pageable pageable);

    Optional<Member> getMemberById(UUID memberId);

    void updateMember(Member member);

    void deleteMemberById(UUID memberId);

    boolean memberExists(UUID memberId);
}
