package cf.thegc.bugatti.service;

import cf.thegc.bugatti.dao.MediaDao;
import cf.thegc.bugatti.model.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MediaService {

    private final MediaDao mediaDao;

    @Autowired
    public MediaService(@Qualifier("postgres") MediaDao mediaDao) {
        this.mediaDao = mediaDao;
    }

    public Page<Media> getMedia(Pageable pageable) {
        return mediaDao.getMedia(pageable);
    }

    public Media addMedia(Media media) {
        return mediaDao.addMedia(media);
    }

    public Optional<Media> getMediaById(UUID memberId) {
        return mediaDao.getMediaById(memberId);
    }

    public int updateMediaById(UUID mediaId, Media newMedia) {
        return mediaDao.updateMediaById(mediaId, newMedia);
    }

    public int deleteMediaById(UUID mediaId) {
        return mediaDao.deleteMediaById(mediaId);
    }
}
