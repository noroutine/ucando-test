package me.noroutine.ucando;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.UUID;
import java.util.UnknownFormatConversionException;

/**
 * Created by oleksii on 07/08/14.
 */

public class Document {

    private UUID uuid;

    private String fileName;

    private String uploadedBy;

    private Date documentDate;

    private Date uploadTime;

    private byte[] content;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
