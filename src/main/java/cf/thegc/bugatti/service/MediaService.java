package cf.thegc.bugatti.service;

import cf.thegc.bugatti.dao.MediaDao;
import cf.thegc.bugatti.exception.BodyParamsException;
import cf.thegc.bugatti.exception.ResourceNotFoundException;
import cf.thegc.bugatti.model.Media;
import cf.thegc.bugatti.model.Member;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.*;
import java.util.stream.Stream;

@Service
public class MediaService {

    private final MediaDao mediaDao;

    @Autowired
    private MemberService memberService;

    private final static Set<String> ALLOWED_FILE_TYPES = new HashSet<>(Arrays.asList("jpg", "jpeg", "png", "gif"));

    // AWS Properties for media upload
    @Value("${aws_client_region}")
    private String AWS_CLIENT_REGION;

    @Value("${aws_s3_media_bucket_name}")
    private String AWS_S3_MEDIA_BUCKET_NAME;

    @Autowired
    public MediaService(@Qualifier("postgres") MediaDao mediaDao) {
        this.mediaDao = mediaDao;
    }

    public List<Media> getMedia(Pageable pageable) {
        return mediaDao.getAllMedia(pageable);
    }

    public Map addMedia(Media media) {

        // Checks for a null media object
        if (media == null) {
            throw new BodyParamsException(BodyParamsException.MEDIA_OBJECT_MISSING);
        }

        // Checks for a null file type
        if (media.getFileType() == null) {
            throw new BodyParamsException(BodyParamsException.MISSING_PARAMS);
        }

        // Checks for an unsupported file type
        if (!ALLOWED_FILE_TYPES.contains(media.getFileType().toLowerCase())) {
            throw new BodyParamsException(BodyParamsException.INVALID_FILETYPE);
        }

        // Checks for a null Member
        if (media.getUploader() == null) {
            throw new BodyParamsException(BodyParamsException.MEMBER_OBJECT_MISSING);
        }

        // Checks for a null Member ID
        if (media.getUploader().getMemberId() == null) {
            throw new BodyParamsException(BodyParamsException.MISSING_MEMBER_ID);
        }

        // Ensures the uploader exists as a member
        Member uploader = memberService.getMemberById(media.getUploader().getMemberId());
        media.setUploader(uploader);

        /*
        Saves the Media object in the database before an S3 presigned URL is requested
        The DAO sets the Media ID, which is needed to generate a presigned URL
         */
        HashMap<String, Object> responseMapping = new HashMap<>();
        responseMapping.put("media", mediaDao.addMedia(media));

        /*
        The Media ID is no longer null, so generate a presigned URL using the Media's ID
         */
        URL presignedUrl = generatePresignedS3URL(media.getMediaId());
        responseMapping.put("presignedUrl", presignedUrl);
        return responseMapping;
    }

    public Optional<Media> getMediaById(UUID mediaId) {
        return mediaDao.getMediaById(mediaId);
    }

    public void updateMedia(Media updatedMedia) {
        Optional<Media> optionalMedia = getMediaById(updatedMedia.getMediaId());
        optionalMedia.orElseThrow(() ->
                new ResourceNotFoundException("Media", updatedMedia.getMediaId()));
        Media existingMedia = optionalMedia.get();

        // Check (and update) title
        if (updatedMedia.getTitle() != null) existingMedia.setTitle(updatedMedia.getTitle());

        // Check (and update) description
        if (updatedMedia.getDescription() != null) existingMedia.setDescription(updatedMedia.getTitle());

        // Check (and update) file type
        if (updatedMedia.getFileType() != null) existingMedia.setFileType(updatedMedia.getFileType());

        // Check (and update) visibility
        if (updatedMedia.getVisible() != null) existingMedia.setVisible(updatedMedia.getVisible());

        // Check (and update) media date
        if (updatedMedia.getMediaDate() != null) existingMedia.setMediaDate(updatedMedia.getMediaDate());

        mediaDao.updateMedia(updatedMedia);
    }

    public void addMembersToMedia(UUID mediaId, Set<UUID> memberIds) {
        // Checks for null media ID
        if (mediaId == null) {
            throw new BodyParamsException(BodyParamsException.MISSING_MEDIA_ID);
        }

        // Checks for null set of Member IDs
        if (memberIds == null) {
            throw new BodyParamsException("Missing list of Member IDs");
        }

        // Checks for non-existent Media
        Optional<Media> optionalMedia = getMediaById(mediaId);
        optionalMedia.orElseThrow(() -> new ResourceNotFoundException("Media", mediaId));
        Media media = optionalMedia.get();

        // Creates a set of Members
        // Request fails if any one Member does not exist
        Set<Member> memberSet = new HashSet<>();
        memberIds.forEach(memberId ->
                memberSet.add(memberService.getMemberById(memberId)));

        memberSet.forEach(member -> media.getMembers().add(member));
        mediaDao.updateMedia(media);
    }

    public void deleteMediaById(UUID mediaId) {
        mediaDao.deleteMediaById(mediaId);
    }

    private URL generatePresignedS3URL(UUID mediaId) {
        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(AWS_CLIENT_REGION)
                    .withCredentials(new ProfileCredentialsProvider())
                    .build();

            // Set the presigned URL to expire after one hour.
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 60 * 60;
            expiration.setTime(expTimeMillis);

            // Generate the presigned URL.
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(AWS_S3_MEDIA_BUCKET_NAME, mediaId.toString())
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);
            return s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        } catch (Exception e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
