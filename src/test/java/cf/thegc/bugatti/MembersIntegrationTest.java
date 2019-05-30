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
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BugattiApplication.class)
public class MembersIntegrationTest {

    @Autowired
    private MemberService memberService;

    /**
     * Steps:
     * 1) Assert no members exist
     * 2) Create and add a member
     * 3) Assert one member exists
     * 4) Perform member lookup using a member ID
     * 5) Update a member
     * 6) Delete a member
     */
    @Test
    public void main() {
        // Asserts no members exist
        List<LimitedMember> members = memberService.getAllMembers(null);
        assertEquals(members.size(), 0);

        // Creates and adds a member
        Member member = new Member()
                .setFirstname("George")
                .setLastname("Washington");
        memberService.addMember(member);

        // Asserts one member exists
        assertEquals(1, memberService.getAllMembers(null).size());
        assertNotNull(memberService.getAllMembers(null).get(0));

        // Performs a member lookup using a member ID
        UUID memberId = memberService.getAllMembers(null).get(0).getMemberId();
        member = memberService.getMemberById(memberId);

        // Update a member
        assertTrue(member.getActive());
        assertNull(member.getPhone());
        assertNull(member.getNickname());

        member.setActive(false)
                .setPhone("0123456789")
                .setNickname("Father of His Country");
        memberService.updateMember(member);

        member = memberService.getMemberById(memberId);
        assertFalse(member.getActive());
        assertNotNull(member.getPhone());
        assertNotNull(member.getNickname());

        // Deletes a member
        memberService.deleteMemberById(memberId);
        assertEquals(0, memberService.getAllMembers(null).size());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void doLookupOnNonExistingMember() {
        assertEquals(0, memberService.getAllMembers(null).size());
        UUID randomUUID = UUID.randomUUID();
        Member member = memberService.getMemberById(randomUUID);
    }
}
