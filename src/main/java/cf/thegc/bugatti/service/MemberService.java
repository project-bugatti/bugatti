package cf.thegc.bugatti.service;

import cf.thegc.bugatti.dao.MemberDao;
import cf.thegc.bugatti.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    public List<Member> getAllMembers() {
        return memberDao.getAllMembers();
    }

    public Optional<Member> getMemberById(UUID memberId) {
        return memberDao.getMemberById(memberId);
    }

    public int updateMemberById(UUID memberId, Member member) {
        return memberDao.updateMemberById(memberId, member);
    }

    public Boolean toggleActiveStatus() {
        return memberDao.toggleActiveStatus();
    }
}
