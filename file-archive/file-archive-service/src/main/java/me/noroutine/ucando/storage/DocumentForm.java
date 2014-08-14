package me.noroutine.ucando.storage;

import me.noroutine.ucando.DocumentMetadata;
import me.noroutine.ucando.storage.jpa.orm.DocumentMetadataMapping;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

/**
 * Created by oleksii on 10/08/14.
 */
public class DocumentForm {

    @FormParam("metadata")
    @PartType(MediaType.APPLICATION_JSON)
    private DocumentMetadata documentMetadata;


    @FormParam("content")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private InputStream content;

    public DocumentMetadata getDocumentMetadata() {
        return documentMetadata;
    }

    public void setDocumentMetadataMapping(DocumentMetadata documentMetadata) {
        this.documentMetadata = documentMetadata;
    }

    public InputStream getContent() {
        return content;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }
}