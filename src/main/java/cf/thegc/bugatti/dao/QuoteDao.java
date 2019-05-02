package cf.thegc.bugatti.dao;

import cf.thegc.bugatti.model.Quote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface QuoteDao {
    Quote addQuote(UUID quoteId, Quote quote);

    default Quote addQuote(Quote quote) {
        UUID quoteId = UUID.randomUUID();
        return addQuote(quoteId, quote);
    }

    Page<Quote> getQuotes(Pageable pageable);

    Optional<Quote> getQuoteById(UUID quoteId);

    int deleteQuoteById(UUID quoteId);

    int updateQuoteTextById(UUID quoteId, Quote newQuote);

}
