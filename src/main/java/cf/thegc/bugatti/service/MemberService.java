package cf.thegc.bugatti.service;

import cf.thegc.bugatti.dao.LimitedMember;
import cf.thegc.bugatti.dao.MemberDao;
import cf.thegc.bugatti.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Optional<Member> getMemberById(UUID memberId) {
        return memberDao.getMemberById(memberId);
    }

    public int updateMemberById(UUID memberId, Member member) {
        return memberDao.updateMemberById(memberId, member);
    }

    public Boolean toggleActive(UUID memberId) {
        return memberDao.toggleActive(memberId);
    }
}
