package cf.thegc.bugatti.dao;

import cf.thegc.bugatti.model.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface MediaDao {

    Page<Media> getMedia(Pageable pageable);

    Media addMedia(UUID mediaId, Media media);

    default Media addMedia(Media media) {
        UUID mediaId = UUID.randomUUID();
        return addMedia(mediaId, media);
    }

    Optional<Media> getMediaById(UUID mediaId);

    int deleteMediaById(UUID mediaId);

    int updateMediaById(UUID mediaId, Media newMedia);
}
