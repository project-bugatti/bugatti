package cf.thegc.bugatti.api;

import cf.thegc.bugatti.exception.ResourceNotFoundException;
import cf.thegc.bugatti.model.Member;
import cf.thegc.bugatti.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api/v1/members")
@RestController
public class MemberController {

    private static final int MEMBERS_PAGE_SIZE = 25;
    private final MemberService memberService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public List getAllMembers(@PageableDefault(size = MEMBERS_PAGE_SIZE ) Pageable pageable) {
        logger.debug("Received GET request for all members");
        return memberService.getAllMembers(pageable);
    }

    @GetMapping(path = "{memberId}")
    public Member getMemberById(@PathVariable("memberId") UUID memberId) {
        logger.debug("Received GET request for member with ID " + memberId);
        return memberService.getMemberById(memberId);
    }

    @PostMapping
    public Member addMember(@RequestBody Member member) {
        logger.debug("Received POST request to add member");
        return memberService.addMember(member);
    }

    @PutMapping(path = "{memberId}")
    public void updateMemberById(@PathVariable("memberId") UUID memberId, @RequestBody Member member) {
        logger.debug("Received PUT request to update member with ID" + memberId);
        member.setMemberId(memberId);
        memberService.updateMember(member);
    }
}
