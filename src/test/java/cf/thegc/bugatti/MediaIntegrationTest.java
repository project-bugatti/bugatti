package cf.thegc.bugatti;

import cf.thegc.bugatti.exception.ResourceNotFoundException;
import cf.thegc.bugatti.model.Media;
import cf.thegc.bugatti.model.Member;
import cf.thegc.bugatti.service.MediaService;
import cf.thegc.bugatti.service.MemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = BugattiApplication.class)
public class MediaIntegrationTest {

    @Autowired
    private MediaService mediaService;

    @Autowired
    private MemberService memberService;


    /**
     * Steps:
     * Assert no members exist
     * Assert no media exists
     * Create and add a member
     * Create and add a media item
     * Lookup media item by ID
     * Update a media item
     * Add a member to a media item
     * Delete a media item
     */
    @Test
    @Transactional // Prevents LazyInitializationException
    public void main() {

        // Asserts no members exists
        assertEquals(0, memberService.getAllMembers(null).size());

        // Asserts no media exists
        assertEquals(0, mediaService.getMedia(null).size());

        // Creates and adds a member
        Member member = new Member().setFirstname("John").setLastname("Adams");
        memberService.addMember(member);
        assertEquals(1, memberService.getAllMembers(null).size());
        assertNotNull(memberService.getAllMembers(null).get(0));

        // Creates and adds a media item
        Media media = new Media().setFileType("jpg").setUploader(member);
        mediaService.addMedia(media);
        assertEquals(1, mediaService.getMedia(null).size());
        assertNotNull(mediaService.getMedia(null).get(0));

        // Looks up a media item by ID
        UUID mediaId = media.getMediaId();
        media = mediaService.getMediaById(mediaId);
        assertNotNull(media);

        // Updates a media item
        assertNull(media.getTitle());
        assertTrue(media.getVisible());
        media.setTitle("Beach day").setVisible(false);
        mediaService.updateMedia(media);

        media = mediaService.getMedia(null).get(0);
        assertNotNull(media.getTitle());
        assertFalse(media.getVisible());

        // Adds a member to a media item
        UUID memberId = memberService.getAllMembers(null).get(0).getMemberId();
        Set<UUID> setMemberIds = new HashSet<>();
        setMemberIds.add(memberId);
        mediaService.modifyMembersOnMedia(mediaId, setMemberIds, true);
        assertEquals(memberId, mediaService.getMediaById(mediaId).getMembers().get(0).getMemberId());

        // Removes a member from a media item
        mediaService.modifyMembersOnMedia(mediaId, setMemberIds, false);
        assertEquals(0, mediaService.getMediaById(mediaId).getMembers().size());

        // Deletes a media item
        media = mediaService.getMedia(null).get(0);
        mediaService.deleteMediaById(media.getMediaId());
        assertEquals(0, mediaService.getMedia(null).size());
    }

    /*
    Performs a lookup on a non-existing media using a random UUID
    Test should throw an exception
     */
    @Test(expected = ResourceNotFoundException.class)
    public void lookupNonExistingMedia() {
        assertEquals(0, mediaService.getMedia(null).size());
        UUID randomUUID = UUID.randomUUID();
        mediaService.getMediaById(randomUUID);
    }

    /*
    Attempts to add a media item with a null member
    Test should throw an exception
     */
    public void addMediaWithNullMember() {
        Media media = new Media()
                .setFileType("jpg")
                .setUploader(null);
        mediaService.addMedia(media);
    }

    /*
    Attempts to add a media item with a non-existing member
    Test should throw an exception
     */
    public void addMediaWithNonExistingMember() {
        Member member = new Member()
                .setFirstname("James")
                .setLastname("Monroe")
                .setMemberId(UUID.randomUUID());
        Media media = new Media()
                .setFileType("jpg")
                .setUploader(member);
        mediaService.addMedia(media);
    }
}
