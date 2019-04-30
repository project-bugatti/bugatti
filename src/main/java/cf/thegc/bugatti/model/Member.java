package cf.thegc.bugatti.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "members")
public class Member extends AuditModel {

    @Id
    @Column(name = "member_id")
    @JsonProperty("member_id")
    @GeneratedValue(generator = "member_generator")
    @GenericGenerator(
            name = "member_generator",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID memberId;

    @Column(name = "firstname")
    @JsonProperty("firstname")
    private String firstname;

    @Column(name = "lastname")
    @JsonProperty("lastname")
    private String lastname;

    @Column(name = "nickname")
    @JsonProperty("nickname")
    private String nickname;

    @Column(name = "phone")
    @JsonProperty("phone")
    private String phone;

    @Column(name = "is_active")
    @JsonProperty("is_active")
    private boolean isActive;

    @JsonProperty("quotes")
    @JoinColumn(name="member_id")
    private List<Quote> quotes;

    public UUID getMemberId() {
        return memberId;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isActive() {
        return isActive;
    }

    public Boolean toggleActive() {
        isActive = !isActive;
        return isActive;
    }

    public List<Quote> getQuotes() {
        return quotes;
    }
}
