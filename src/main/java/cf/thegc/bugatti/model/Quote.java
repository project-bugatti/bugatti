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
    @JoinColumn(name = "author_member_id")
    private Member author;

    public UUID getQuoteId() {
        return quoteId;
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

    public Long getQuoteDate() {
        return quoteDate;
    }

    public Member getAuthor() {
        return author;
    }
}
