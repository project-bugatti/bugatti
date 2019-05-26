package cf.thegc.bugatti;

import cf.thegc.bugatti.service.MediaService;
import cf.thegc.bugatti.service.MemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
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
     * 1) Assert no members exist
     * 2) Assert no media exists
     * 3) Create and add a member
     * 4) Create and add a media item
     * 5) Lookup media item by ID
     * 6) Update a media item
     * 7) Delete a media item
     */
    @Test
    public void main() {

        // Asserts no members exists
        assertEquals(0, memberService.getMembers(null).size());

        // Asserts no media exists
        assertEquals(0, mediaService.getMedia(null).size());

        //

    }
}
