package cf.thegc.bugatti.model;

import cf.thegc.bugatti.dao.LimitedMember;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
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
    @JsonProperty("media_id")
    @GeneratedValue(generator = "media_generator")
    @GenericGenerator(
            name = "mediaer_generator",
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

    @NotNull
    @JsonProperty("uploader")
    @ManyToOne()
    @JoinColumn(name = "uploader_member_id")
    @JsonIgnoreProperties("quotes") // prevent recursion / stack overflow
    private Member uploader;

//    @ManyToMany
//    @JoinTable(
//            name = "members_media",
//            joinColumns = { @JoinColumn(name = "media_id") },
//            inverseJoinColumns = { @JoinColumn(name = "member_id") }
//    )
//    @NotNull
//    @JsonProperty(value = "members")
//    @JsonIgnoreProperties("quotes") // prevents recursion / stack overflow
//    private List<Member> members;

    public UUID getMediaId() {
        return mediaId;
    }

    public Media setMediaId(UUID mediaId) {
        this.mediaId = mediaId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Media setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Media setDescription(String description) {
        this.description = description;
        return this;
    }

    public Date getMediaDate() {
        return mediaDate;
    }

    public Media setMediaDate(Date mediaDate) {
        this.mediaDate = mediaDate;
        return this;
    }

    public String getFileType() {
        return fileType;
    }

    public Media setFileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    public Boolean getVisible() {
        return visible;
    }

    public Media setVisible(Boolean visible) {
        this.visible = visible;
        return this;
    }

    public Member getUploader() {
        return uploader;
    }

    public Media setUploader(Member uploader) {
        this.uploader = uploader;
        return this;
    }
}
