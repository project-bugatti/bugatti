package cf.thegc.bugatti.service;

import cf.thegc.bugatti.dao.MediaDao;
import cf.thegc.bugatti.exception.BodyParamsException;
import cf.thegc.bugatti.model.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
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

    public Page<Media> getMedia(Pageable pageable) {
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

    public Optional<Media> getMediaById(UUID memberId) {
        return mediaDao.getMediaById(memberId);
    }

    public void updateMediaById(UUID mediaId, Media newMedia) {
        mediaDao.updateMediaById(mediaId, newMedia);
    }

    public void deleteMediaById(UUID mediaId) {
        mediaDao.deleteMediaById(mediaId);
    }
}
