package me.noroutine.ucando;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by oleksii on 07/08/14.
 */
public class FileArchiveJAXRSRepository implements FileArchiveRepository {

    private Client client;

    private String baseUrl;

    public FileArchiveJAXRSRepository() {
        this.client = ClientBuilder.newClient();
    }

    @Override
    public boolean createDocument(DocumentMetadata documentMetadata) {
        return client.target(baseUrl)
                .request()
                .post(Entity.json(documentMetadata), Boolean.class);
    }

    @Override
    public List<DocumentMetadata> searchByUploader(String uploader) {
        return client.target(baseUrl)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<DocumentMetadata>>() {});
    }

    @Override
    public List<DocumentMetadata> searchByUploadedTime(Date from, Date to) {
        return client.target(baseUrl)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<DocumentMetadata>>() {});
    }

    @Override
    public DocumentMetadata getById(String uuid) {
        return client.target(baseUrl)
                .path(uuid)
                .request(MediaType.APPLICATION_JSON)
                .get(DocumentMetadata.class);
    }

    @Override
    public List<DocumentMetadata> findAll() {
        return client.target(baseUrl)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<DocumentMetadata>>() {});
    }

    @Override
    public boolean delete(String uuid) {
        return client.target(baseUrl)
                .path(uuid)
                .request()
                .delete(Boolean.class);
    }

    @Override
    public boolean createDocument(DocumentMetadata documentMetadata, InputStream content) {
        return this.createDocument(documentMetadata) && this.setContent(documentMetadata.getUuid(), content);
    }

    @Override
    public boolean setContent(String uuid, InputStream input) {
        MultipartFormDataOutput form = new MultipartFormDataOutput();
        form.addFormData("content", input, MediaType.APPLICATION_OCTET_STREAM_TYPE);

        return client.target(baseUrl)
                .path(uuid + "/content")
                .request()
                .post(Entity.entity(new GenericEntity<MultipartFormDataOutput>(form) {}, MediaType.MULTIPART_FORM_DATA_TYPE), Boolean.class);
    }

    @Override
    public byte[] getContent(String uuid) {
        return new byte[0];
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}

