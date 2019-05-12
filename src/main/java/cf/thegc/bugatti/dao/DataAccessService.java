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
    public List<LimitedMember> getMembers(Pageable pageable) {
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

    // Quote methods

    @Override
    public List<Quote> getQuotes(Pageable pageable) {
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

    // Media methods

    @Override
    public List<Media> getMedia(Pageable pageable) {
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
    public String getPresignedUrl(Media media) {
        String clientRegion = "us-east-1";
        String bucketName = "media.thegc.";
        String objectKey = media.getMediaId().toString();

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(new ProfileCredentialsProvider())
                    .build();

            // Set the presigned URL to expire after one hour.
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 60 * 60;
            expiration.setTime(expTimeMillis);

            // Generate the presigned URL.
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, objectKey)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);
            URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
            return url.toString();
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteMediaById(UUID mediaId) {
        mediaRepository.deleteById(mediaId);
    }

    @Override
    public void updateMediaById(UUID mediaId, Media newMedia) {
        mediaRepository.save(newMedia);
    }
}
