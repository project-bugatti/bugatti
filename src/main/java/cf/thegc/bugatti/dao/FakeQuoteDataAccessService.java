package cf.thegc.bugatti.dao;

import cf.thegc.bugatti.model.Quote;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("fakeDao")
public class FakeQuoteDataAccessService implements QuoteDao {

    private static List<Quote> DB = new ArrayList<>();

    @Override
    public Quote addQuote(UUID quoteId, Quote quote) {
        DB.add(new Quote(quote.getQuoteText(), quote.getAuthorMemberId(), quote.getVisible(), quote.getQuoteDate()));
        return quote;
    }

    @Override
    public List<Quote> getAllQuotes() {
        return DB;
    }

    @Override
    public Optional<Quote> getQuoteById(UUID quoteId) {
        return DB.stream()
                .filter(quote -> quote.getQuoteId().equals(quoteId))
                .findFirst();
    }

    @Override
    public int deleteQuoteById(UUID quoteId) {
        Optional<Quote> quoteMaybe = getQuoteById(quoteId);
        if (quoteMaybe.isPresent()) {
            return 0;
        }
        DB.remove(quoteMaybe.get());
        return 1;
    }

    @Override
    public int updateQuoteTextById(UUID quoteId, Quote update) {
        return getQuoteById(quoteId)
                .map(quote -> {
                    int indexOfQuoteToUpdate = DB.indexOf(quote);
                    if (indexOfQuoteToUpdate >= 0) {
                        DB.set(indexOfQuoteToUpdate, null);
                        return 1;
                    }
                    return 0;
                })
                .orElse(0);
    }


}
