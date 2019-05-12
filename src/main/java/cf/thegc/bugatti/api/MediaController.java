package cf.thegc.bugatti.api;

import cf.thegc.bugatti.model.Media;
import cf.thegc.bugatti.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/api/v1/media")
@RestController
public class MediaController {

    private static final int MEDIA_PAGE_SIZE = 10;
    private final MediaService mediaService;

    @Autowired
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping
    public List getMedia(@PageableDefault(size = MEDIA_PAGE_SIZE)Pageable pageable) {
        return mediaService.getMedia(pageable);
    }

    @GetMapping(path = "{mediaId}")
    public Media getMediabyId(@PathVariable("mediaId") UUID mediaId) {
        return mediaService.getMediaById(mediaId).orElse(null);
    }

    @PostMapping
    public Map addMedia(@RequestBody Media media) {
        return mediaService.addMedia(media);
    }

    @PutMapping(path = "{mediaId}")
    public void updateMediaById(@PathVariable("mediaId") UUID mediaId, @RequestBody Media media) {
        mediaService.updateMediaById(mediaId, media);
    }

    @DeleteMapping(path = "{mediaId}")
    public void deleteMediaById(@PathVariable("mediaId") UUID mediaId) {
        mediaService.deleteMediaById(mediaId);
    }
}
