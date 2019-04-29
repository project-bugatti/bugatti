package cf.thegc.bugatti.service;

import cf.thegc.bugatti.dao.QuoteDao;
import cf.thegc.bugatti.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    public Quote addQuote(Quote quote) {
        return quoteDao.addQuote(quote);
    }

    public List<Quote> getAllQuotes() {
        return quoteDao.getAllQuotes();
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
