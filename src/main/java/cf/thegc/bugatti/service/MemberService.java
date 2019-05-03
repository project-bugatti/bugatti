package cf.thegc.bugatti.service;

import cf.thegc.bugatti.dao.LimitedMember;
import cf.thegc.bugatti.dao.MemberDao;
import cf.thegc.bugatti.exception.MemberNotFoundException;
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

    public static final String ERROR_NO_MEMBER_EXISTS = "No member exists";

    private final MemberDao memberDao;

    @Autowired
    public MemberService(@Qualifier("postgres") MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Page<LimitedMember> getMembers(Pageable pageable) {
        return memberDao.getMembers(pageable);
    }

    public Optional<Member> getMemberById(UUID memberId) {
        Optional<Member> member = memberDao.getMemberById(memberId);
        if (!member.isPresent()) {
            throw new MemberNotFoundException(memberId);
        }
        return member;
    }

    public int updateMemberById(UUID memberId, Member member) {
        return memberDao.updateMemberById(memberId, member);
    }

    public Boolean toggleActive(UUID memberId) {
        return memberDao.toggleActive(memberId);
    }
}
