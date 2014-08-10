package me.noroutine.ucando;

import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
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

    private String fileArchiveManagerUser;

    private String fileArchiveManagerPassword;

    public FileArchiveJAXRSRepository() {
        this.client = ClientBuilder.newClient();
    }

    @Override
    public boolean createDocument(DocumentMetadata documentMetadata) {
        return client.target(baseUrl)
                .register(new BasicAuthentication(fileArchiveManagerUser, fileArchiveManagerPassword))
                .request()
                .post(Entity.json(documentMetadata), Boolean.class);
    }

    @Override
    public List<DocumentMetadata> searchByUploader(String uploader) {
        return client.target(baseUrl)
                .path("/filter/uploadedBy")
                .queryParam("uploadedBy", uploader)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<DocumentMetadata>>() {});
    }

    @Override
    public List<DocumentMetadata> searchByUploadedTime(long from, long to) {
        return client.target(baseUrl)
                .path("/filter/uploadedTime")
                .queryParam("from", from)
                .queryParam("to", to)
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
                .register(new BasicAuthentication(fileArchiveManagerUser, fileArchiveManagerPassword))
                .request()
                .delete(Boolean.class);
    }

    @Override
    public boolean createDocument(DocumentMetadata documentMetadata, InputStream content) {
        MultipartFormDataOutput form = new MultipartFormDataOutput();
        form.addFormData("metadata", documentMetadata, MediaType.APPLICATION_JSON_TYPE);
        form.addFormData("content", content, MediaType.APPLICATION_OCTET_STREAM_TYPE);

        return client.target(baseUrl)
                .register(new BasicAuthentication(fileArchiveManagerUser, fileArchiveManagerPassword))
                .request()
                .post(Entity.entity(new GenericEntity<MultipartFormDataOutput>(form) {}, MediaType.MULTIPART_FORM_DATA_TYPE), Boolean.class);
    }

    @Override
    public boolean setContent(String uuid, InputStream content) {
        MultipartFormDataOutput form = new MultipartFormDataOutput();
        form.addFormData("content", content, MediaType.APPLICATION_OCTET_STREAM_TYPE);

        return client.target(baseUrl)
                .path(uuid + "/content")
                .register(new BasicAuthentication(fileArchiveManagerUser, fileArchiveManagerPassword))
                .request()
                .post(Entity.entity(new GenericEntity<MultipartFormDataOutput>(form) {}, MediaType.MULTIPART_FORM_DATA_TYPE), Boolean.class);
    }

    @Override
    public InputStream getContentAsStream(String uuid) {
        return client.target(baseUrl)
                .path(uuid + "/content")
                .request()
                .accept(MediaType.APPLICATION_OCTET_STREAM_TYPE)
                .get(new GenericType<InputStream>() {
                });
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getFileArchiveManagerUser() {
        return fileArchiveManagerUser;
    }

    public void setFileArchiveManagerUser(String fileArchiveManagerUser) {
        this.fileArchiveManagerUser = fileArchiveManagerUser;
    }

    public String getFileArchiveManagerPassword() {
        return fileArchiveManagerPassword;
    }

    public void setFileArchiveManagerPassword(String fileArchiveManagerPassword) {
        this.fileArchiveManagerPassword = fileArchiveManagerPassword;
    }
}

