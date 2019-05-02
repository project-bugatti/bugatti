package cf.thegc.bugatti.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "quotes")
public class Quote extends AuditModel {
    @Id
    @Column(name = "quote_id")
    @JsonProperty("quote_id")
    @GeneratedValue(generator = "quote_generator")
    @GenericGenerator(
            name = "quote_generator",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID quoteId;

    @Column(name = "quote_text")
    @JsonProperty("quote_text")
    private String quoteText;

    @Column(name = "visible")
    @JsonProperty("visible")
    private Boolean visible = true;

    @Column(name = "quote_date")
    @JsonProperty("quote_date")
    private Long quoteDate;

    @JsonProperty("member")
    @ManyToOne()
    @JoinColumn(name = "author_member_id")
    @JsonIgnoreProperties("quotes") // prevent recursion / stack overflow
    private Member member;

    public UUID getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(UUID quoteId) {
        this.quoteId = quoteId;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Long getQuoteDate() {
        return quoteDate;
    }

    public void setQuoteDate(Long quoteDate) {
        this.quoteDate = quoteDate;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public String toString() {

        /*
        Quote: "Ask not what your country can do for you — ask what you can do for your country
            - John Kennedy (Qoute ID: c25ae6d7-43a9-4f7e-ba35-91c611aee4ad)
         */

        StringBuilder quoteBuilder = new StringBuilder();
        quoteBuilder
                .append("Quote: ").append(quoteText).append("\n\t")
                .append("- ").append(member.getFirstname()).append(" ").append(member.getLastname())
                .append(" (Quote ID: ").append(quoteId).append(")");
        return quoteBuilder.toString();
    }
}
