package cf.thegc.bugatti;

import cf.thegc.bugatti.model.Member;
import cf.thegc.bugatti.model.Quote;
import cf.thegc.bugatti.service.MemberService;
import cf.thegc.bugatti.service.QuoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResourceAccessException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BugattiApplication.class)
public class QuotesIntegrationTest {

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private MemberService memberService;

    @Test
    public void givenQuoteService_doCRUD_thenOK() {
        // Get all quotes
        List<Quote> quotes = quoteService.getAllQuotes(null);
        assert quotes.size() == 0;

        // Add quote (add member first)
        Member member = new Member();
        member.setFirstname("Stonewall");
        member.setLastname("Jackson");
        memberService.addMember(member);

        Quote quote = new Quote();
        quote.setQuoteText("Hello, world!");
        quote.setMember(member);
        quoteService.addQuote(quote);

        // Get all quotes
        quotes = quoteService.getAllQuotes(null);
        assert quotes.size() == 1;

        // Get quote
        Quote foundQuote = quoteService.getQuoteById(quotes.get(0).getQuoteId());
        assertNotNull(foundQuote);
        assertEquals(quotes.get(0).getQuoteText(), foundQuote.getQuoteText());

        // Update quote
        assertTrue(foundQuote.getVisible());
        assertNull(foundQuote.getQuoteDate());
        foundQuote.setVisible(false);
        foundQuote.setQuoteDate(new Date().getTime());
        quoteService.updateQuoteTextById(foundQuote.getQuoteId(), foundQuote);

        foundQuote = quoteService.getQuoteById(foundQuote.getQuoteId());
        assertFalse(foundQuote.getVisible());
        assertNotNull(foundQuote.getQuoteDate());

        // Delete Quote
        quoteService.deleteQuoteById(foundQuote.getQuoteId());
        assert quoteService.getAllQuotes(null).size() == 0;
    }

    @Test(expected = ResourceAccessException.class)
    public void givenQuoteService_doLookupOnNonExistingQuote() {
        assert quoteService.getAllQuotes(null).size() == 0;
        UUID randomUUID = UUID.randomUUID();
        Quote quote = quoteService.getQuoteById(randomUUID);
    }

}
