package cf.thegc.bugatti.service;

import cf.thegc.bugatti.dao.LimitedMember;
import cf.thegc.bugatti.dao.MemberDao;
import cf.thegc.bugatti.exception.ResourceNotFoundException;
import cf.thegc.bugatti.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MemberService {

    private final MemberDao memberDao;

    @Autowired
    public MemberService(@Qualifier("postgres") MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Page<LimitedMember> getMembers(Pageable pageable) {
        return memberDao.getMembers(pageable);
    }

    public Member getMemberById(UUID memberId) {
        Optional<Member> member = memberDao.getMemberById(memberId);
        member.orElseThrow(() -> new ResourceNotFoundException(memberId, "Member"));
        return member.get();
    }

    public void updateMember(UUID memberId, Member updatedMember) {
        Member existingMember = getMemberById(memberId);

        // Check (and update) nickname
        if (updatedMember.getNickname() != null) existingMember.setNickname(updatedMember.getNickname());

        // Check (and update) phone
        if (updatedMember.getPhone() != null) existingMember.setPhone(updatedMember.getPhone());

        // Check (and update) active status
        if (updatedMember.getActive() != null) existingMember.setActive(updatedMember.getActive());

        memberDao.updateMember(existingMember);
    }
}
