package cf.thegc.bugatti.api;

import cf.thegc.bugatti.model.Quote;
import cf.thegc.bugatti.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity getQuotes(@PageableDefault(size = QUOTES_PAGE_SIZE) Pageable pageable) {
        return new ResponseEntity<>(quoteService.getAllQuotes(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "{quoteId}")
    public Quote getQuoteById(@PathVariable("quoteId") UUID quoteId) {
        return quoteService.getQuoteById(quoteId);
    }

    @PostMapping
    private ResponseEntity addQuote(@RequestBody Quote incomingQuote) {
        return new ResponseEntity<>(quoteService.addQuote(incomingQuote), HttpStatus.OK);
    }

    @PutMapping(path = "{quoteId}")
    public void updateQuoteById(@PathVariable("quoteId") UUID quoteId, @RequestBody Quote quoteToUpdate) {
        quoteToUpdate.setQuoteId(quoteId);
        quoteService.updateQuoteById(quoteToUpdate);
    }

    @DeleteMapping(path = "{quoteId}")
    public void deleteQuoteById(@PathVariable("quoteId") UUID quoteId) {
        quoteService.deleteQuoteById(quoteId);
    }
}
