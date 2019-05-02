package cf.thegc.bugatti.api;

import cf.thegc.bugatti.dao.LimitedMember;
import cf.thegc.bugatti.model.Member;
import cf.thegc.bugatti.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public List<LimitedMember> getMembers(@PageableDefault(value=3, page = 0)Pageable pageable) {
        Page page = memberService.getMembers(pageable);
        return page.getContent();
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
