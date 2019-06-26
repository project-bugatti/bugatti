package cf.thegc.bugatti;

import cf.thegc.bugatti.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private MemberService memberService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${auth0.client_id}")
    private String AUTH0_CLIENT_ID;

    @Value("${auth0.client_secret}")
    private String AUTH0_CLIENT_SECRET;

    @Value("${auth0.audience}")
    private String AUTH0_AUDIENCE;

    @Value("${auth0.grant_type}")
    private String AUTH0_GRANT_TYPE;

    /**
     * - Generate a JWT from Auth0 using a client credentials grant
     * - Add a member
     * - Get all members (array of type LimitedMember)
     * - Get the Member ID of the Member in position zero
     * - Perform a Member lookup by that ID
     * - Update the Member
     * - Delete the Member
     *
     * @throws Exception - MockMvc exception
     */
    @Test
    public void main() throws Exception {
        // Generates a JWT
        String url = "https://thegc.auth0.com/oauth/token";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> bodyParameters = new ArrayList<>();
        bodyParameters.add(new BasicNameValuePair("client_id", AUTH0_CLIENT_ID));
        bodyParameters.add(new BasicNameValuePair("client_secret", AUTH0_CLIENT_SECRET));
        bodyParameters.add(new BasicNameValuePair("audience", AUTH0_AUDIENCE));
        bodyParameters.add(new BasicNameValuePair("grant_type", AUTH0_GRANT_TYPE));
        httpPost.setEntity(new UrlEncodedFormEntity(bodyParameters));

        CloseableHttpResponse response = httpclient.execute(httpPost);
        String body = EntityUtils.toString(response.getEntity());
        String accessToken = new JSONObject(body).getString("access_token");

        // Adds a member
        String member = "{\"firstname\" : \"John\", \"lastname\" : \"Tyler\"}";

        MvcResult result = this.mvc.perform(post("/api/v1/members")
                .header("Authorization", "Bearer " + accessToken)
                .content(member)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        logger.debug(getPrettyJson(responseBody));

        // Gets all members
        result = this.mvc.perform(get("/api/v1/members"))
                .andExpect(status().isOk())
                .andReturn();
        responseBody = result.getResponse().getContentAsString();
        JSONArray jsonArrayAllMembers = new JSONArray(responseBody);
        logger.debug(getPrettyJson(responseBody));

        // Gets the ID of the Member in position 0
        JSONObject jsonObject = jsonArrayAllMembers.getJSONObject(0);
        String memberId = jsonObject.getString("member_id");

        // Performs a Member lookup by ID
        result = this.mvc.perform(get("/api/v1/members/" + memberId))
                .andExpect(status().isOk())
                .andReturn();
        responseBody = result.getResponse().getContentAsString();
        logger.debug(getPrettyJson(responseBody));

        // Updates the member
        String updateToPerform = "{\"lastname\" : \"Jacobs\", \"phone\" : \"8675309\"}";
        this.mvc.perform(put("/api/v1/members/" + memberId)
                .content(updateToPerform)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Deletes all members
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
