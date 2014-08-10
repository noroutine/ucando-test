package me.noroutine.ucando.orm;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Blob;
import java.util.Date;

/**
 * Created by oleksii on 09/08/14.
 */
@Entity
@Table(name = "documents")
@NamedQueries({
        @NamedQuery(name = "documents.findAll", query = "select dm from DocumentMetadata dm")
})
public class DocumentMetadata {

    private String uuid;

    private String fileName;

    private String uploadedBy;

    private Date documentDate;

    private Date uploadTime;

    private Blob content;

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

    @Basic(fetch = FetchType.LAZY)
    @Lob
    @Column(name = "content")
    @JsonIgnore
    public Blob getContent() {
        return content;
    }

    public void setContent(Blob content) {
        this.content = content;
    }
}