package cf.thegc.bugatti.dao;

import cf.thegc.bugatti.model.Member;
import cf.thegc.bugatti.model.Quote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres")
public class PostgresDataAccessService implements QuoteDao, MemberDao {

    private final QuoteRepository quoteRepository;
    private final MemberRepository memberRepository;

    public PostgresDataAccessService(QuoteRepository quoteRepository,
                                     MemberRepository memberRepository) {
        this.quoteRepository = quoteRepository;
        this.memberRepository = memberRepository;
    }

    // Member methods


    @Override
    public Page<LimitedMember> getMembers(Pageable pageable) {
        return memberRepository.getAllBy(pageable);
    }

    @Override
    public Optional<Member> getMemberById(UUID memberId) {
        return memberRepository.findById(memberId);
    }

    @Override
    public int updateMemberById(UUID memberId, Member newMember) {
        return getMemberById(memberId).map(existingMember -> {
            if (newMember.getNickname() != null) {
                existingMember.setNickname(newMember.getNickname());
            }
            if (newMember.getPhone() != null) {
                existingMember.setPhone(newMember.getPhone());
            }
            memberRepository.save(existingMember);
            return 1;
        }).orElse(0);
    }

    @Override
    public Boolean toggleActive(UUID memberId) {
        return getMemberById(memberId).map(Member::toggleActive).orElse(null);
    }

    // Quote methods

    @Override

    public List<Quote> getAllQuotes() {
        return quoteRepository.findAll();
    }

    @Override
    public Quote addQuote(UUID quoteId, Quote quote) {
        return quoteRepository.save(quote);
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
}
