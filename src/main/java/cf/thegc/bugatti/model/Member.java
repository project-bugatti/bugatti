package cf.thegc.bugatti.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
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

    @Column(name = "active")
    @JsonProperty("active")
    private Boolean active = true;

    @JsonProperty("quotes")
    @OneToMany(mappedBy = "member")
    @JsonIgnoreProperties("member") // prevent recursion / stack overflow
    private List<Quote> quotes;

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<Quote> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<Quote> quotes) {
        this.quotes = quotes;
    }

    public Boolean toggleActive() {
        this.active = !active;
        return this.active;
    }

    @Override
    public String toString() {

        /*
        George Washington ("Georgie")
            Phone: 9127894512
            Status: active
            ID: b74f56ff-b560-4a25-b48a-daf7f1b6ff51
         */

        StringBuilder memberBuilder = new StringBuilder();
        memberBuilder
                .append(firstname).append(" ").append(lastname);
        if(nickname != null) {
            memberBuilder.append(" (\"").append(nickname).append("\")");
        }
        memberBuilder
                .append("\n\t")
                .append("Phone: ").append(phone).append("\n\t")
                .append("Status: ").append(active ? "active" : "inactive").append("\n\t")
                .append("ID: ").append(memberId);
        return memberBuilder.toString();
    }
}
