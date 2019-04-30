package cf.thegc.bugatti.dao;

import cf.thegc.bugatti.model.Member;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberDao {

    List<Member> getAllMembers();

    Optional<Member> getMemberById(UUID memberId);

    int updateMemberById(UUID memberId, Member member);

    Boolean toggleActive(UUID memberId);

}
