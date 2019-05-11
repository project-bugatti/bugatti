package cf.thegc.bugatti.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "media")
public class Media extends AuditModel {

    enum FileType {
        JPG,
        JPEG,
        PNG,
        GIF;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    @Id
    @Column(name = "media_id")
    @JsonProperty(value = "media_id")
    @GeneratedValue(generator = "media_generator")
    @GenericGenerator(
            name = "media_generator",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID mediaId;

    @Column(name = "title")
    @JsonProperty(value = "title")
    private String title;

    @Column(name = "description")
    @JsonProperty(value = "description")
    private String description;

    @Column(name = "media_date")
    @JsonProperty(value = "media_date")
    private Date mediaDate;

    @NotNull
    @Column(name = "file_type")
    @JsonProperty(value = "file_type")
    private String fileType;

    @Column(name = "visible")
    @JsonProperty(value = "visible")
    private Boolean visible = true;

    public UUID getMediaId() {
        return mediaId;
    }

    public void setMediaId(UUID mediaId) {
        this.mediaId = mediaId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getMediaDate() {
        return mediaDate;
    }

    public void setMediaDate(Date mediaDate) {
        this.mediaDate = mediaDate;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}
