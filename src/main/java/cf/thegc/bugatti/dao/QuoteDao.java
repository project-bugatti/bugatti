package cf.thegc.bugatti.dao;

import cf.thegc.bugatti.model.Quote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuoteDao {
    Quote addQuote(Quote quote);

    List<Quote> getAllQuotes(Pageable pageable);

    Optional<Quote> getQuoteById(UUID quoteId);

    int deleteQuoteById(UUID quoteId);

    int updateQuote(Quote newQuote);

}
