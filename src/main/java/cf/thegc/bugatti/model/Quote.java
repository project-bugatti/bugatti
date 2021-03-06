package cf.thegc.bugatti.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @NotNull
    @Column(name = "quote_text")
    @JsonProperty("quote_text")
    private String quoteText;

    @Column(name = "visible")
    @JsonProperty("visible")
    private Boolean visible = true;

    @Column(name = "quote_date")
    @JsonProperty("quote_date")
    private Long quoteDate;

    @NotNull
    @JsonProperty("author")
    @ManyToOne()
    @JoinColumn(name = "author_member_id")
    @JsonIgnoreProperties("quotes") // prevent recursion / stack overflow
    private Member author;

    public UUID getQuoteId() {
        return quoteId;
    }

    public Quote setQuoteId(UUID quoteId) {
        this.quoteId = quoteId;
        return this;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public Quote setQuoteText(String quoteText) {
        this.quoteText = quoteText;
        return this;
    }

    public Boolean getVisible() {
        return visible;
    }

    public Quote setVisible(Boolean visible) {
        this.visible = visible;
        return this;
    }

    public Long getQuoteDate() {
        return quoteDate;
    }

    public Quote setQuoteDate(Long quoteDate) {
        this.quoteDate = quoteDate;
        return this;
    }

    public Member getAuthor() {
        return author;
    }

    public Quote setAuthor(Member author) {
        this.author = author;
        return this;
    }

    @Override
    public String toString() {

        /*
        "Ask not what your country can do for you — ask what you can do for your country"
            Author: John Kennedy
            Quote ID: c25ae6d7-43a9-4f7e-ba35-91c611aee4ad
            Date: null
            Visible: true
         */

        return new StringBuilder()
                .append("\"").append(quoteText).append("\"").append("\n\t")
                .append("Author: ").append(author.getFirstname()).append(" ").append(author.getLastname()).append("\n\t")
                .append("Quote ID: ").append(quoteId).append("\n\t")
                .append("Date: ").append(quoteDate).append("\n\t")
                .append("Visible: ").append(visible)
                .toString();
    }
}
