package me.noroutine.ucando.metadata.cassandra.mapping;

/**
 * Created by oleksii on 17/08/14.
 */

import me.noroutine.ucando.DocumentMetadata;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.Table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by oleksii on 09/08/14.
 */
@Table("documents")
//@NamedQueries({
//        @NamedQuery(name = "documents.findAll", query = "select dm from DocumentMetadataMapping dm"),
//        @NamedQuery(name = "documents.findByUploader", query = "select dm from DocumentMetadataMapping dm where dm.uploadedBy = :uploadedBy"),
//        @NamedQuery(name = "documents.findByUploadTimeRange", query = "select dm from DocumentMetadataMapping dm where dm.uploadTime between :from_time and :to_time"),
//        @NamedQuery(name = "documents.findBydocumentDateRange", query = "select dm from DocumentMetadataMapping dm where dm.documentDate between :from_time and :to_time")
//})
public class DocumentMetadataMapping {

    private UUID uuid;

    private String fileName;

    private String uploadedBy;

    private Date documentDate;

    private Date uploadTime;

    @Id
    @Column("uuid")
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Column("file_name")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column("uploaded_by")
    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    @Column("document_date")
    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    @Column("upload_time")
    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public static DocumentMetadataMapping wrap(DocumentMetadata metadata) {
        if (metadata == null) {
            return null;
        }

        DocumentMetadataMapping wrapped = new DocumentMetadataMapping();
        wrapped.setUuid(UUID.fromString(metadata.getUuid()));
        wrapped.setFileName(metadata.getFileName());
        wrapped.setDocumentDate(metadata.getDocumentDate());
        wrapped.setUploadedBy(metadata.getUploadedBy());
        wrapped.setUploadTime(metadata.getUploadTime());
        return wrapped;

    }

    public static DocumentMetadata unwrap(DocumentMetadataMapping metadataMapping) {
        if (metadataMapping == null) {
            return null;
        }

        DocumentMetadata unwrapped = new DocumentMetadata();
        unwrapped.setUuid(metadataMapping.getUuid().toString());
        unwrapped.setFileName(metadataMapping.getFileName());
        unwrapped.setDocumentDate(metadataMapping.getDocumentDate());
        unwrapped.setUploadedBy(metadataMapping.getUploadedBy());
        unwrapped.setUploadTime(metadataMapping.getUploadTime());
        return unwrapped;
    }

    public static List<DocumentMetadata> unwrapList(List<DocumentMetadataMapping> metadataList) {
        if (metadataList == null) {
            return null;
        }

        List<me.noroutine.ucando.DocumentMetadata> unwrapped = new ArrayList<>(metadataList.size());
        for (DocumentMetadataMapping metadata: metadataList) {
            unwrapped.add(DocumentMetadataMapping.unwrap(metadata));
        }

        return unwrapped;
    }
}