package cf.thegc.bugatti.service;

import cf.thegc.bugatti.dao.QuoteDao;
import cf.thegc.bugatti.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuoteService {

    private final QuoteDao quoteDao;

    @Autowired
    public QuoteService(@Qualifier("postgres") QuoteDao quoteDao) {
        this.quoteDao = quoteDao;
    }

    /**
     * @param quote The new quote to insert
     * @return The quote that was successfully inserted, which includes the quote identifier
     */
    public Quote addQuote(Quote quote) {
        return quoteDao.addQuote(quote);
    }

    public Page<Quote> getQuotes(Pageable pageable) {
        return quoteDao.getQuotes(pageable);
    }

    public Optional<Quote> getQuoteById(UUID quoteId) {
        return quoteDao.getQuoteById(quoteId);
    }

    public int updateQuoteTextById(UUID quoteId, Quote newQuote) {
        return quoteDao.updateQuoteTextById(quoteId, newQuote);
    }

    public int deleteQuoteById(UUID quoteId) {
        return quoteDao.deleteQuoteById(quoteId);
    }
}
