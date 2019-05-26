package cf.thegc.bugatti.service;

import cf.thegc.bugatti.dao.MediaDao;
import cf.thegc.bugatti.exception.BodyParamsException;
import cf.thegc.bugatti.exception.ResourceNotFoundException;
import cf.thegc.bugatti.model.Media;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
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

@Service
public class MediaService {

    private final MediaDao mediaDao;
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
        return mediaDao.getMedia(pageable);
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

        HashMap<String, Object> responseMapping = new HashMap<>();
        URL presignedUrl = generatePresignedS3URL(media.getMediaId());
        responseMapping.put("media", mediaDao.addMedia(media));
        responseMapping.put("presignedUrl", presignedUrl);
        return responseMapping;
    }

    public Media getMediaById(UUID mediaId) {
        Optional<Media> media = mediaDao.getMediaById(mediaId);
        media.orElseThrow(() -> new ResourceNotFoundException("Media", mediaId));
        return media.get();
    }

    public void updateMedia(Media updatedMedia) {
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
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
