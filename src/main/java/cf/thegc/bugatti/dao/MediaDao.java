package cf.thegc.bugatti.dao;

import cf.thegc.bugatti.model.Media;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MediaDao {

    List<Media> getAllMedia(Pageable pageable);

    Media addMedia(Media media);

    Optional<Media> getMediaById(UUID mediaId);

    void deleteMediaById(UUID mediaId);

    void updateMedia(Media updatedMedia);
}
