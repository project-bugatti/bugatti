package cf.thegc.bugatti.api;

import cf.thegc.bugatti.model.Quote;
import cf.thegc.bugatti.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/quotes")
@RestController
public class QuoteController {

    private final QuoteService quoteService;

    @Autowired
    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping
    public List<Quote> getAllQuotes() {
        return quoteService.getAllQuotes();
    }

    @PostMapping
    private Quote addQuote(@RequestBody Quote quote) {
        return quoteService.addQuote(quote);
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
