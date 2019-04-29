package cf.thegc.bugatti.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "quotes")
public class Quote extends AuditModel {
    @Id
    @Column(name = "quote_id")
    @GeneratedValue(generator = "quote_generator")
    @GenericGenerator(
            name = "quote_generator",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID quoteId;

    @Column(name = "quote_text")
    private String quoteText;

    @Column(name = "author_member_id")
    private UUID authorMemberId;

    @Column(name = "is_visible")
    private Boolean isVisible;

    @Column(name = "quote_date")
    private Long quoteDate;


    private Member member;

    public Quote() {
    }

    public Quote(@JsonProperty("quote_text") String quoteText,
                 @JsonProperty("author_member_id") UUID authorMemberId,
                 @JsonProperty("is_visible") Boolean isVisible,
                 @JsonProperty("quote_date") Long quoteDate) {
        this.quoteId = UUID.randomUUID();
        this.quoteText = quoteText;
        this.authorMemberId = authorMemberId;
        this.isVisible = isVisible == null ? true : isVisible;
        this.quoteDate = quoteDate;
    }

    public UUID getQuoteId() {
        return quoteId;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }

    public UUID getAuthorMemberId() {
        return authorMemberId;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public Long getQuoteDate() {
        return quoteDate;
    }
}
