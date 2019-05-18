package cf.thegc.bugatti.service;

import cf.thegc.bugatti.dao.QuoteDao;
import cf.thegc.bugatti.exception.ResourceNotFoundException;
import cf.thegc.bugatti.model.Member;
import cf.thegc.bugatti.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuoteService {

    private final QuoteDao quoteDao;
    private final MemberService memberService;

    @Autowired
    public QuoteService(@Qualifier("postgres") QuoteDao quoteDao,
                        MemberService memberService) {
        this.quoteDao = quoteDao;
        this.memberService = memberService;
    }

    /**
     * The member service gets the author, or throws an error if the member doesn't exist
     * The quote service inserts the quote and returns the quote
     * The quote sets its author
     *
     * @param incomingQuote The quote to be inserted
     * @return The quote that was successfully inserted, which includes the quote identifier
     */
    public Quote addQuote(Quote incomingQuote) {
        UUID memberId = incomingQuote.getMember().getMemberId();
        Member author = memberService.getMemberById(memberId);

        Quote newQuote = quoteDao.addQuote(incomingQuote);
        newQuote.setMember(author);
        return newQuote;
    }

    public List<Quote> getAllQuotes(Pageable pageable) {
        return quoteDao.getAllQuotes(pageable);
    }

    public Quote getQuoteById(UUID quoteId) {
        Optional<Quote> quote = quoteDao.getQuoteById(quoteId);
        quote.orElseThrow(() -> new ResourceNotFoundException(quoteId, "Quote"));
        return quote.get();
    }

    public void updateQuoteById(Quote updatedQuote) {
        Quote existingQuote = getQuoteById(updatedQuote.getQuoteId());

        // Check (and update) quote text
        if (updatedQuote.getQuoteText() != null) existingQuote.setQuoteText(updatedQuote.getQuoteText());

        // Check (and update) quote visibility
        if (updatedQuote.getVisible() != null) existingQuote.setVisible(updatedQuote.getVisible());

        // Check (and update) date
        if (updatedQuote.getQuoteDate() != null) existingQuote.setQuoteDate(updatedQuote.getQuoteDate());

        // Check (and update) member
        if (updatedQuote.getMember() != null) existingQuote.setMember(updatedQuote.getMember());

        quoteDao.updateQuote(existingQuote);
    }

    public void deleteQuoteById(UUID quoteId) {
        quoteDao.deleteQuoteById(quoteId);
    }
}
