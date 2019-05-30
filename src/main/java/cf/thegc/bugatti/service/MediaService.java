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
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.*;

@Service
public class MediaService {

    private final MediaDao mediaDao;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

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

    public Media getMediaById(UUID mediaId) {
        Optional<Media> optionalMedia = mediaDao.getMediaById(mediaId);
        optionalMedia.orElseThrow(() -> {
            logger.error("Rejected the request to get a non-existent media with ID " + mediaId);
            return new ResourceNotFoundException("Media", mediaId);
        });
        return optionalMedia.get();
    }

    public void updateMedia(Media updatedMedia) {
        // Check for null media
        if (updatedMedia == null) {
            throw new BodyParamsException(BodyParamsException.MEDIA_OBJECT_MISSING);
        }

        // Check for null media ID
        if (updatedMedia.getMediaId() == null) {
            throw new BodyParamsException(BodyParamsException.MISSING_MEDIA_ID);
        }

        // Check for non-existent media
        UUID mediaId = updatedMedia.getMediaId();
        Media existingMedia = getMediaById(updatedMedia.getMediaId());

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
        logger.info("Updated media with ID " + mediaId);
    }

    public void modifyMembersOnMedia(UUID mediaId, Set<UUID> setMemberIds, boolean addMembers) {
        // Checks for null media ID
        if (mediaId == null) {
            throw new BodyParamsException(BodyParamsException.MISSING_MEDIA_ID);
        }

        // Checks for null set of Member IDs
        if (setMemberIds == null) {
            throw new BodyParamsException("Missing list of Member IDs");
        }

        // Checks for non-existent Media
        Media media = getMediaById(mediaId);

        // Creates a set of Members by performing lookups by ID
        // Request fails if any one Member does not exist
        Set<Member> memberSet = new HashSet<>();
        setMemberIds.forEach(memberId -> memberSet.add(memberService.getMemberById(memberId)));

        if (addMembers) {
            // Adds each member to the media
            memberSet.forEach(member -> {
                media.addMember(member);
                logger.debug("Added member with ID " + member.getMemberId() + " to media with ID " + media.getMediaId());
            });
        } else {
            // Removes each member from the media
            memberSet.forEach(member -> {
                Hibernate.initialize(media.getMembers());
                media.removeMember(member);
                logger.debug("Removed member with ID " + member.getMemberId() + " from media with ID " + media.getMediaId());
            });
        }
        mediaDao.updateMedia(media);
        logger.info("Updated media with ID " + mediaId);
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

            // Set the presigned URL to expire after one hour
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 60 * 60;
            expiration.setTime(expTimeMillis);

            // Generate the presigned URL
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(AWS_S3_MEDIA_BUCKET_NAME, mediaId.toString())
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);
            URL presignedUrl = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
            logger.info("Generated an S3 presigned URL for media with ID " + mediaId);
            return presignedUrl;
        } catch (Exception e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
    }
}
