package cf.thegc.bugatti;

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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 1) Add one or more members and log each response
     * 2) Get all members, of type LimitedMember, and log the response
     * 3) Get the Member ID of the Member in position zero
     * 4) Perform a Member lookup by that ID
     * 5) Update the Member
     * 6) Delete all Members
     *
     * @throws Exception - MockMvc exception
     */
    @Test
    public void main() throws Exception {
        // 1) Adds one or more members and logs each response
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> members = new ArrayList<>();
        members.add("{\"firstname\" : \"John\", \"lastname\" : \"Tyler\"}");
        members.add("{\"firstname\" : \"Calvin\", \"lastname\" : \"Coolidge\"}");

        for (String member : members) {
            try {
                MvcResult result = this.mvc.perform(post("/api/v1/members")
                        .content(member)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();
                String responseBody = result.getResponse().getContentAsString();
                logger.debug(getPrettyJson(responseBody));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 2) Gets all members and logs the response
        MvcResult result = this.mvc.perform(get("/api/v1/members"))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        logger.debug(getPrettyJson(responseBody));

        // 3) Gets the ID of the Member in position 0
        JSONArray jsonArray = new JSONArray(responseBody);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        String memberId = jsonObject.getString("member_id");

        // 4) Performs a Member lookup by ID
        result = this.mvc.perform(get("/api/v1/members/" + memberId))
                .andExpect(status().isOk())
                .andReturn();
        responseBody = result.getResponse().getContentAsString();
        logger.debug(getPrettyJson(responseBody));

        // 5) Updates the member
        String updateToPerform = "{\"phone\" : \"8675309\"}";
        this.mvc.perform(put("/api/v1/members/" + memberId)
                .content(updateToPerform)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 6) Deletes all members
        result = this.mvc.perform(get("/api/v1/members"))
                .andExpect(status().isOk())
                .andReturn();
        responseBody = result.getResponse().getContentAsString();
        jsonArray = new JSONArray(responseBody);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject memberToDelete = jsonArray.getJSONObject(i);
            memberId = memberToDelete.getString("member_id");
            this.mvc.perform(delete("/api/v1/members/" + memberId))
                    .andExpect(status().isOk());
        }

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
