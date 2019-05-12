package cf.thegc.bugatti.dao;

import cf.thegc.bugatti.model.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface MediaDao {

    List<Media> getMedia(Pageable pageable);

    Media addMedia(Media media);

    String getPresignedUrl(Media media);

    Optional<Media> getMediaById(UUID mediaId);

    void deleteMediaById(UUID mediaId);

    void updateMediaById(UUID mediaId, Media newMedia);
}
