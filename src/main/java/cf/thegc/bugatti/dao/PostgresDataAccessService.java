package cf.thegc.bugatti.dao;

import cf.thegc.bugatti.model.Member;
import cf.thegc.bugatti.model.Quote;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres")
public class PostgresDataAccessService implements QuoteDao, MemberDao {

    private final QuoteRepository quoteRepository;
    private final MemberRepository memberRepository;

    public PostgresDataAccessService(QuoteRepository quoteRepository, MemberRepository memberRepository) {
        this.quoteRepository = quoteRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public List<Quote> getAllQuotes() {
        return quoteRepository.findAll();
    }

    @Override
    public Quote addQuote(UUID quoteId, Quote quote) {
        return quoteRepository.save(new Quote(quote.getQuoteText(),
                quote.getAuthorMemberId(),
                quote.getVisible(),
                quote.getQuoteDate()));
    }

    @Override
    public Optional<Quote> getQuoteById(UUID quoteId) {
        return quoteRepository.findById(quoteId);
    }

    @Override
    public int updateQuoteTextById(UUID quoteId, Quote newQuote) {
        return getQuoteById(quoteId)
                .map(existingQuote -> {
                    existingQuote.setQuoteText(newQuote.getQuoteText());
                    quoteRepository.save(existingQuote);
                    return 1;
                }).orElse(0);
    }

    @Override
    public int deleteQuoteById(UUID quoteId) {
        quoteRepository.deleteById(quoteId);
        return 1;
    }

    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Optional<Member> getMemberById(UUID memberId) {
        return memberRepository.findById(memberId);
    }

    @Override
    public int updateMemberById(UUID memberId, Member member) {
        return 0;
    }

    @Override
    public Boolean toggleActiveStatus() {
        return null;
    }
}
