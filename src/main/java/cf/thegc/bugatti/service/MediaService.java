package cf.thegc.bugatti.service;

import cf.thegc.bugatti.dao.MediaDao;
import cf.thegc.bugatti.exception.BodyParamsException;
import cf.thegc.bugatti.exception.ResourceNotFoundException;
import cf.thegc.bugatti.model.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MediaService {

    private final MediaDao mediaDao;
    private final static Set<String> ALLOWED_FILE_TYPES = new HashSet<>(Arrays.asList("jpg", "jpeg", "png", "gif"));

    @Autowired
    public MediaService(@Qualifier("postgres") MediaDao mediaDao) {
        this.mediaDao = mediaDao;
    }

    public List<Media> getMedia(Pageable pageable) {
        return mediaDao.getMedia(pageable);
    }

    public Map addMedia(Media media) {

        // Throws an exception if the file type parameter is missing
        if (media.getFileType() == null) {
            throw new BodyParamsException(BodyParamsException.MISSING_PARAMS);
        }

        // Throws an exception if the file type parameter is not supported
        if (!ALLOWED_FILE_TYPES.contains(media.getFileType().toLowerCase())) {
            throw new BodyParamsException(BodyParamsException.INVALID_FILETYPE);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("media", mediaDao.addMedia(media));
        map.put("presignedUrl", mediaDao.getPresignedUrl(media));
        return map;
    }

    public Media getMediaById(UUID mediaId) {
        Optional<Media> media = mediaDao.getMediaById(mediaId);
        media.orElseThrow(() -> new ResourceNotFoundException(mediaId, "Media"));
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
}
