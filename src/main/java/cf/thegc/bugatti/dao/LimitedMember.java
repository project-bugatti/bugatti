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

    String toString();

    default String getLimitedMemberToString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("Name: ").append(getFirstname()).append(" ").append(getLastname());

        // Append nickname if one exists
        if (getNickname() != null) {
            stringBuilder.append("(").append(getNickname()).append(")");
        }

        stringBuilder
                .append("\n\t")
                .append("ID: ").append(getMemberId())
                .append("\n\t")
                .append("Active: ").append(getActive());
        return stringBuilder.toString();
    }
}
