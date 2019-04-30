package cf.thegc.bugatti.api;

import cf.thegc.bugatti.model.Member;
import cf.thegc.bugatti.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping(path = "{memberId}")
    public Member getMemberById(@PathVariable("memberId") UUID memberId) {
        return memberService.getMemberById(memberId).orElse(null);
    }

    @PutMapping(path = "{memberId}")
    public void updateMemberById(@PathVariable("memberId") UUID memberId, @RequestBody Member member) {
        memberService.updateMemberById(memberId, member);
    }

    @GetMapping
    @RequestMapping("/toggleStatus")
    public void toggleActive(@PathVariable("memberId") UUID memberId) {
        memberService.toggleActive(memberId);
    }
}
