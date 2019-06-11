package cf.thegc.bugatti.service;

import cf.thegc.bugatti.dao.LimitedMember;
import cf.thegc.bugatti.dao.MemberDao;
import cf.thegc.bugatti.exception.BodyParamsException;
import cf.thegc.bugatti.exception.ResourceNotFoundException;
import cf.thegc.bugatti.model.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MemberService {

    private final MemberDao memberDao;
    private final AuthenticationService authenticationService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final HttpServletRequest httpServletRequest;

    @Autowired
    public MemberService(@Qualifier("postgres") MemberDao memberDao,
                         AuthenticationService authenticationService,
                         HttpServletRequest httpServletRequest) {
        this.memberDao = memberDao;
        this.authenticationService = authenticationService;
        this.httpServletRequest = httpServletRequest;
    }

    public Member addMember(Member memberToAdd) {
        Member addedMember = memberDao.addMember(memberToAdd);
        logger.info("Added a new member " + addedMember.toString());
        return addedMember;
    }

    public List<LimitedMember> getAllMembers(Pageable pageable) {
        List<LimitedMember> allMembers = memberDao.getAllMembers(pageable);
        logger.info("Retrieved all members");
        return allMembers;
    }

    public Member getMemberById(UUID memberId) {
        authenticationService.verifyJWT(httpServletRequest);

        Optional<Member> optionalMember = memberDao.getMemberById(memberId);
        optionalMember.orElseThrow(() -> {
            logger.error("Rejected the request to get a non-existent member with ID " + memberId);
            return new ResourceNotFoundException("Member", memberId);
        });
        logger.info("Retrieved the member with ID " + memberId);
        return optionalMember.get();
    }

    public void updateMember(Member updatedMember) {
        // Check for null Member
        if (updatedMember == null) {
            throw new BodyParamsException(BodyParamsException.MEMBER_OBJECT_MISSING);
        }

        // Check for null Member ID
        if (updatedMember.getMemberId() == null) {
            throw new BodyParamsException(BodyParamsException.MISSING_MEMBER_ID);
        }

        UUID memberId = updatedMember.getMemberId();
        Member existingMember = getMemberById(memberId);

        // Check (and update) first name
        if (updatedMember.getFirstname() != null) existingMember.setFirstname(updatedMember.getFirstname());

        // Check (and update) last name
        if (updatedMember.getLastname() != null) existingMember.setLastname(updatedMember.getLastname());

        // Check (and update) nickname
        if (updatedMember.getNickname() != null) existingMember.setNickname(updatedMember.getNickname());

        // Check (and update) phone
        if (updatedMember.getPhone() != null) existingMember.setPhone(updatedMember.getPhone());

        // Check (and update) active status
        if (updatedMember.getActive() != null) existingMember.setActive(updatedMember.getActive());

        memberDao.updateMember(existingMember);
        logger.info("Updated member with Id " + memberId);
    }

    public void deleteMemberById(UUID memberId) {
        if (memberExists(memberId)) {
            memberDao.deleteMemberById(memberId);
            logger.info("Deleted member with ID " + memberId);
        } else {
            logger.error("Rejected the request to delete a non-existent member with ID " + memberId);
            throw new ResourceNotFoundException(memberId);
        }
    }

    public boolean memberExists(UUID memberId) {
        boolean exists = memberDao.memberExists(memberId);
        logger.info("Request to determine Member existence with ID " + memberId + " returned " + exists);
        return exists;
    }
}
