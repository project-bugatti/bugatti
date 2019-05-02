package cf.thegc.bugatti.service;

import cf.thegc.bugatti.api.ApiPostException;
import cf.thegc.bugatti.dao.MemberDao;
import cf.thegc.bugatti.dao.QuoteDao;
import cf.thegc.bugatti.model.Member;
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
    private final MemberDao memberDao;

    @Autowired
    public QuoteService(@Qualifier("postgres") QuoteDao quoteDao,
                        @Qualifier("postgres") MemberDao memberDao) {
        this.quoteDao = quoteDao;
        this.memberDao = memberDao;
    }

    /**
     * The member service gets the author, or throws an error if the member doesn't exist
     * The quote service inserts the quote and returns the quote
     * The quote sets its author
     *
     * @param incomingQuote The quote to be inserted
     * @return The quote that was successfully inserted, which includes the quote identifier
     */
    public Quote addQuote(Quote incomingQuote) throws ApiPostException {
        Member author = memberDao.getMemberById(incomingQuote.getMember().getMemberId()).orElse(null);
        if (author == null) {
            throw new ApiPostException(MemberService.ERROR_NO_MEMBER_EXISTS);
        }

        Quote newQuote = quoteDao.addQuote(incomingQuote);
        newQuote.setMember(author);
        return newQuote;
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
