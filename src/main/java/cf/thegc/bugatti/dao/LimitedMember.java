package cf.thegc.bugatti.dao;

import java.util.UUID;

public interface LimitedMember {
    UUID getMemberId();
    String getFirstname();
    String getLastname();
    String getNickname();
    String getActive();
}
