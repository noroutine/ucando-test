package me.noroutine.ucando.metadata.orm;

import me.noroutine.ucando.DocumentMetadata;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by oleksii on 09/08/14.
 */
@Entity
@Table(name = "documents")
@NamedQueries({
        @NamedQuery(name = "documents.findAll", query = "select dm from DocumentMetadataMapping dm where dm.deleted = false"),
        @NamedQuery(name = "documents.findByUploader", query = "select dm from DocumentMetadataMapping dm where dm.uploadedBy = :uploadedBy and dm.deleted = false"),
        @NamedQuery(name = "documents.findByUploadTimeRange", query = "select dm from DocumentMetadataMapping dm where dm.uploadTime between :from_time and :to_time and dm.deleted = false"),
        @NamedQuery(name = "documents.findBydocumentDateRange", query = "select dm from DocumentMetadataMapping dm where dm.documentDate between :from_time and :to_time and dm.deleted = false")
})
public class DocumentMetadataMapping {

    private String uuid;

    private String fileName;

    private String uploadedBy;

    private Date documentDate;

    private Date uploadTime;

    private boolean deleted;

    @Id
    @Column(name = "uuid")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Basic
    @Column(name = "file_name")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Basic
    @Column(name = "uploaded_by")
    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    @Basic
    @Column(name = "document_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    @Basic
    @Column(name = "upload_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    @Basic
    @Column(name = "deleted")
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public static DocumentMetadataMapping wrap(DocumentMetadata metadata) {
        if (metadata == null) {
            return null;
        }

        DocumentMetadataMapping wrapped = new DocumentMetadataMapping();
        wrapped.setUuid(metadata.getUuid());
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

        if (metadataMapping.isDeleted()) {
            return null;
        }

        DocumentMetadata unwrapped = new DocumentMetadata();
        unwrapped.setUuid(metadataMapping.getUuid());
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

        List<me.noroutine.ucando.DocumentMetadata> unwrappedList = new ArrayList<>(metadataList.size());
        for (DocumentMetadataMapping metadata: metadataList) {
            DocumentMetadata unwrapped = DocumentMetadataMapping.unwrap(metadata);
            if (unwrapped != null) {
                unwrappedList.add(unwrapped);
            }
        }

        return unwrappedList;
    }
}