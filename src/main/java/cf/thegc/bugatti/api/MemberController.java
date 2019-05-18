package cf.thegc.bugatti.api;

import cf.thegc.bugatti.model.Member;
import cf.thegc.bugatti.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/members")
@RestController
public class MemberController {

    private static final int MEMBERS_PAGE_SIZE = 25;
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public List getMembers(@PageableDefault(size = MEMBERS_PAGE_SIZE ) Pageable pageable) {
        return memberService.getMembers(pageable);
    }

    @GetMapping(path = "{memberId}")
    public Member getMemberById(@PathVariable("memberId") UUID memberId) {
        return memberService.getMemberById(memberId);
    }

    @PutMapping(path = "{memberId}")
    public void updateMemberById(@PathVariable("memberId") UUID memberId, @RequestBody Member member) {
        member.setMemberId(memberId);
        memberService.updateMember(member);
    }
}
