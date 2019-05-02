package cf.thegc.bugatti.model;

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

    @Column(name = "is_visible")
    @JsonProperty("is_visible")
    private Boolean isVisible;

    @Column(name = "quote_date")
    @JsonProperty("quote_date")
    private Long quoteDate;

    @JsonProperty("member")
    @ManyToOne
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
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
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
}
