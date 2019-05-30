package cf.thegc.bugatti.dao;

import cf.thegc.bugatti.model.Media;
import cf.thegc.bugatti.model.Member;
import cf.thegc.bugatti.model.Quote;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

@Repository("postgres")
public class DataAccessService implements QuoteDao, MemberDao, MediaDao {

    private final QuoteRepository quoteRepository;
    private final MemberRepository memberRepository;
    private final MediaRepository mediaRepository;

    public DataAccessService(QuoteRepository quoteRepository,
                             MemberRepository memberRepository,
                             MediaRepository mediaRepository) {
        this.quoteRepository = quoteRepository;
        this.memberRepository = memberRepository;
        this.mediaRepository = mediaRepository;
    }

    // Member methods


    @Override
    public Member addMember(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public List<LimitedMember> getAllMembers(Pageable pageable) {
        return memberRepository.getAllBy(pageable).getContent();
    }

    @Override
    public Optional<Member> getMemberById(UUID memberId) {
        return memberRepository.findById(memberId);
    }

    @Override
    public void updateMember(Member member) {
        memberRepository.save(member);
    }

    @Override
    public void deleteMemberById(UUID memberId) {
        memberRepository.deleteById(memberId);
    }

    @Override
    public boolean memberExists(UUID memberId) {
        return memberRepository.existsById(memberId);
    }

    // Quote methods

    @Override
    public List<Quote> getAllQuotes(Pageable pageable) {
        return quoteRepository.getAllBy(pageable).getContent();
    }

    @Override
    public Quote addQuote(Quote quote) {
        return quoteRepository.save(quote);
    }

    @Override
    public Optional<Quote> getQuoteById(UUID quoteId) {
        return quoteRepository.findById(quoteId);
    }

    @Override
    public int updateQuote(Quote newQuote) {
        quoteRepository.save(newQuote);
        return 1;
    }

    @Override
    public int deleteQuoteById(UUID quoteId) {
        quoteRepository.deleteById(quoteId);
        return 1;
    }

    // Media methods

    @Override
    public List<Media> getAllMedia(Pageable pageable) {
        return mediaRepository.getAllBy(pageable).getContent();
    }

    @Override
    public Optional<Media> getMediaById(UUID mediaId) {
        return mediaRepository.findById(mediaId);
    }

    @Override
    public Media addMedia(Media media) {
        return mediaRepository.save(media);
    }

    @Override
    public void deleteMediaById(UUID mediaId) {
        mediaRepository.deleteById(mediaId);
    }

    @Override
    public void updateMedia(Media updatedMedia) {
        mediaRepository.save(updatedMedia);
    }
}
