package cf.thegc.bugatti;

import cf.thegc.bugatti.dao.LimitedMember;
import cf.thegc.bugatti.exception.ResourceNotFoundException;
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
public class MembersIntegrationTest {

    @Autowired
    private MemberService memberService;

    @Test
    public void givenMemberService_doCRUD_thenOK() {
        // Get all members
        List<LimitedMember> members = memberService.getMembers(null);
        assert members.size() == 0;

        // Add member
        Member member0 = new Member();
        member0.setFirstname("George");
        member0.setLastname("Washington");
        memberService.addMember(member0);

        // Get all members
        members = memberService.getMembers(null);
        assert members.size() >= 1;

        // Get member
        Member foundMember = memberService.getMemberById(members.get(0).getMemberId());
        assertNotNull(foundMember);
        assertEquals(members.get(0).getLastname(), foundMember.getLastname());

        // Update member
        assertTrue(foundMember.getActive());
        assertNull(foundMember.getNickname());
        foundMember.setNickname("The Father of His Country");
        foundMember.setActive(false);
        memberService.updateMemberById(foundMember.getMemberId(), foundMember);

        foundMember = memberService.getMemberById(foundMember.getMemberId());
        assertNotNull(foundMember.getNickname());
        assertFalse(foundMember.getActive());

        // Delete member
        memberService.deleteMemberById(foundMember.getMemberId());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void givenMemberService_doLookupOnNonExistingMember() {
        assert memberService.getMembers(null).size() == 0;
        UUID randomUUID = UUID.randomUUID();
        Member member = memberService.getMemberById(randomUUID);
    }
}
