package cf.thegc.bugatti;

import cf.thegc.bugatti.dao.LimitedMember;
import cf.thegc.bugatti.model.Member;
import cf.thegc.bugatti.service.MemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BugattiApplication.class)
public class SpringBootIntegrationTest {

    @Autowired
    private MemberService memberService;

    @Test
    public void givenMemberService_addMemberAndGetAndUpdateAndDelete_thenOK() {
        // Add member
        Member member0 = new Member();
        member0.setFirstname("George");
        member0.setLastname("Washington");
        memberService.addMember(member0);
        List<LimitedMember> members = memberService.getMembers(null);
        assert members.size() == 1;

        // Get member
        Member foundMember = memberService.getMemberById(member0.getMemberId());
        assertNotNull(foundMember);
        assertEquals(member0.getLastname(), foundMember.getLastname());

        // Update member
        foundMember.setNickname("The Father of His Country");
        foundMember.setActive(false);
        memberService.updateMember(foundMember.getMemberId(), foundMember);
        assertNotNull(foundMember.getNickname());
        assertFalse(foundMember.getActive());

        // Delete member
    }
}
