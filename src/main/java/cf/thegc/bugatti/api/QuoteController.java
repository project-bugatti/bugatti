package cf.thegc.bugatti.api;

import cf.thegc.bugatti.model.Member;
import cf.thegc.bugatti.model.Quote;
import cf.thegc.bugatti.service.MemberService;
import cf.thegc.bugatti.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/quotes")
@RestController
public class QuoteController {

    private static final int QUOTES_PAGE_SIZE = 25;
    private final QuoteService quoteService;

    @Autowired
    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping
    public List<Quote> getQuotes(@PageableDefault(size = QUOTES_PAGE_SIZE) Pageable pageable) {
        Page page = quoteService.getQuotes(pageable);
        return page.getContent();
    }

    @PostMapping
    private Quote addQuote(@RequestBody Quote incomingQuote) throws ApiPostException {
        return quoteService.addQuote(incomingQuote);
    }

    @GetMapping(path = "{quoteId}")
    public Quote getQuoteById(@PathVariable("quoteId") UUID quoteId) {
        return quoteService.getQuoteById(quoteId).orElse(null);
    }

    @PutMapping(path = "{quoteId}")
    public void updateQuoteById(@PathVariable("quoteId") UUID quoteId, @RequestBody Quote quoteToUpdate) {
        quoteService.updateQuoteTextById(quoteId, quoteToUpdate);
    }

    @DeleteMapping(path = "{quoteId}")
    public void deleteQuoteById(@PathVariable("quoteId") UUID quoteId) {
        quoteService.deleteQuoteById(quoteId);
    }
}
