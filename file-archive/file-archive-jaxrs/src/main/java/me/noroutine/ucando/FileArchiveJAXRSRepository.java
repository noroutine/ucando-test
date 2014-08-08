package me.noroutine.ucando;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    public void upload(Document document) {
        client.target(baseUrl)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(document));
    }

    @Override
    public List<Document> searchByUploader(String uploader) {
        return client.target(baseUrl)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Document>>() {});
    }

    @Override
    public List<Document> searchByUploadedTime(Date from, Date to) {
        return client.target(baseUrl)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Document>>() {});
    }

    @Override
    public Document getById(UUID uuid) {
        return client.target(baseUrl)
                .path(uuid.toString())
                .request(MediaType.APPLICATION_JSON)
                .get(Document.class);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}

