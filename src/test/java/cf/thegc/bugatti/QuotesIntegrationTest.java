package cf.thegc.bugatti;

import cf.thegc.bugatti.dao.LimitedMember;
import cf.thegc.bugatti.exception.BodyParamsException;
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

    /**
     * Steps:
     * 1) Assert no members exist
     * 2) Assert no quotes exist
     * 3) Create and add a member
     * 4) Assert one member exists
     * 5) Create and add a quote, set the quote's author to the member
     */
    @Test
    public void main() {

        // Asserts no members exist
        assertEquals(0, memberService.getMembers(null).size());

        // Asserts no quotes exist
        assertEquals(0, quoteService.getAllQuotes(null).size());

        // Creates and adds a member
        Member member = new Member()
                .setFirstname("Stonewall")
                .setLastname("Jackson");
        memberService.addMember(member);

        // Asserts one member exists
        List<LimitedMember> members = memberService.getMembers(null);
        assertNotNull(memberService.getMembers(null).get(0));

        // Creates and adds a quote with an author
        UUID memberId = memberService.getMembers(null).get(0).getMemberId();
        member = memberService.getMemberById(memberId);
        Quote quote = new Quote()
                .setQuoteText("Hello, world!")
                .setAuthor(member);

        quoteService.addQuote(quote);

        // Asserts one quote exists
        assertEquals(1, quoteService.getAllQuotes(null).size());
        assertNotNull(quoteService.getAllQuotes(null).get(0));

        // Updates a quote
        quote = quoteService.getAllQuotes(null).get(0);
        assertTrue(quote.getVisible());
        assertNull(quote.getQuoteDate());

        quote.setVisible(false).setQuoteDate(new Date().getTime());
        quoteService.updateQuote(quote);

        Quote sameQuote = quoteService.getAllQuotes(null).get(0);
        assertFalse(sameQuote.getVisible());
        assertNotNull(sameQuote.getQuoteDate());

        // Deletes a quote
        quote = quoteService.getAllQuotes(null).get(0);
        quoteService.deleteQuoteById(quote.getQuoteId());
        assertEquals(0, quoteService.getAllQuotes(null).size());
    }


    /*
    Performs a lookup on a non existing quote using a random UUID
    Test should throw an exception
     */
    @Test(expected = ResourceNotFoundException.class)
    public void india() {
        assertEquals(quoteService.getAllQuotes(null).size(), 0);
        UUID randomUUID = UUID.randomUUID();
        Quote quote = quoteService.getQuoteById(randomUUID);
    }

    /*
    Attempts to add a quote with a null member
    Test should throw an exception
     */
    @Test(expected = BodyParamsException.class)
    public void juliet() {
        Quote quote = new Quote()
                .setQuoteText("Hello, world!")
                .setAuthor(null);
        quoteService.addQuote(quote);
    }

}
