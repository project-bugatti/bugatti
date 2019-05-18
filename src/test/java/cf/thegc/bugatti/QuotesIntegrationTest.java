package cf.thegc.bugatti;

import cf.thegc.bugatti.exception.ResourceNotFoundException;
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
        List<Quote> allQuotes = quoteService.getAllQuotes(null);
        assert allQuotes.size() == 0;

        // Add quote (add member first)
        Member member = new Member()
                .setFirstname("Stonewall")
                .setLastname("Jackson");
        memberService.addMember(member);

        Quote quote = new Quote()
                .setQuoteText("Hello, world!")
                .setMember(member)
                .setVisible(true);
        quoteService.addQuote(quote);

        // Get all quotes
        allQuotes = quoteService.getAllQuotes(null);
        assert allQuotes.size() == 1;

        // Get a single quote
        Quote foundQuote = quoteService.getQuoteById(allQuotes.get(0).getQuoteId());
        assertNotNull(foundQuote);
        assertEquals(allQuotes.get(0).getQuoteText(), foundQuote.getQuoteText());

        // Update quote
        assertTrue(foundQuote.getVisible());
        assertNull(foundQuote.getQuoteDate());
        foundQuote
                .setVisible(false)
                .setQuoteDate(new Date().getTime());
        quoteService.updateQuoteById(foundQuote);

        foundQuote = quoteService.getQuoteById(foundQuote.getQuoteId());

        assertFalse(foundQuote.getVisible());
        assertNotNull(foundQuote.getQuoteDate());

        // Delete Quote
        quoteService.deleteQuoteById(foundQuote.getQuoteId());
        assert quoteService.getAllQuotes(null).size() == 0;
    }

    @Test(expected = ResourceNotFoundException.class)
    public void givenQuoteService_doLookupOnNonExistingQuote() {
        assert quoteService.getAllQuotes(null).size() == 0;
        UUID randomUUID = UUID.randomUUID();
        Quote quote = quoteService.getQuoteById(randomUUID);
    }

}
