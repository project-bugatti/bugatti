package cf.thegc.bugatti;

import cf.thegc.bugatti.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MemberService memberService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 1) Add a member
     * 2) Get all members (array of type LimitedMember)
     * 3) Get the Member ID of the Member in position zero
     * 4) Perform a Member lookup by that ID
     * 5) Update the Member
     * 6) Delete the Member
     *
     * @throws Exception - MockMvc exception
     */
    @Test
    public void main() throws Exception {
        // 1) Adds a member
        String member = "{\"firstname\" : \"John\", \"lastname\" : \"Tyler\"}";

        MvcResult result = this.mvc.perform(post("/api/v1/members")
                .content(member)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        logger.debug(getPrettyJson(responseBody));

        // 2) Gets all members
        result = this.mvc.perform(get("/api/v1/members"))
                .andExpect(status().isOk())
                .andReturn();
        responseBody = result.getResponse().getContentAsString();
        JSONArray jsonArrayAllMembers = new JSONArray(responseBody);
        logger.debug(getPrettyJson(responseBody));

        // 3) Gets the ID of the Member in position 0
        JSONObject jsonObject = jsonArrayAllMembers.getJSONObject(0);
        String memberId = jsonObject.getString("member_id");

        // 4) Performs a Member lookup by ID
        result = this.mvc.perform(get("/api/v1/members/" + memberId))
                .andExpect(status().isOk())
                .andReturn();
        responseBody = result.getResponse().getContentAsString();
        logger.debug(getPrettyJson(responseBody));

        // 5) Updates the member
        String updateToPerform = "{\"lastname\" : \"Jacobs\", \"phone\" : \"8675309\"}";
        this.mvc.perform(put("/api/v1/members/" + memberId)
                .content(updateToPerform)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 6) Deletes all members
        // Uses the service because no member delete endpoint exists
        memberService.deleteMemberById(UUID.fromString(memberId));
        logger.debug("Deleted member with ID " + memberId);
    }

    private static String getPrettyJson(String uglyJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Object jsonObject = objectMapper.readValue(uglyJson, Object.class);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (IOException e) {
            return uglyJson;
        }
    }
}
