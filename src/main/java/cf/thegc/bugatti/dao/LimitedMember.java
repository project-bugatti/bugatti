package cf.thegc.bugatti.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public interface LimitedMember {
    @JsonProperty("member_id")
    UUID getMemberId();

    String getFirstname();

    String getLastname();

    String getNickname();

    String getActive();
}
