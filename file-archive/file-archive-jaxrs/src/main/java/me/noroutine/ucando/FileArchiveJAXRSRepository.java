package me.noroutine.ucando;

import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by oleksii on 07/08/14.
 */
public class FileArchiveJAXRSRepository implements FileArchiveRepository {

    private String baseUrl;

    private String fileArchiveManagerUser;

    private String fileArchiveManagerPassword;

    @Override
    public boolean createDocument(DocumentMetadata documentMetadata) {
        return ClientBuilder.newClient().target(baseUrl)
                .register(new BasicAuthentication(fileArchiveManagerUser, fileArchiveManagerPassword))
                .request()
                .post(Entity.json(documentMetadata), Boolean.class);
    }

    @Override
    public List<DocumentMetadata> searchByUploader(String uploader) {
        return ClientBuilder.newClient().target(baseUrl)
                .path("/filter/uploadedBy")
                .queryParam("uploadedBy", uploader)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<DocumentMetadata>>() {});
    }

    @Override
    public List<DocumentMetadata> searchByUploadedTime(long from, long to) {
        return ClientBuilder.newClient().target(baseUrl)
                .path("/filter/uploadedTime")
                .queryParam("from", from)
                .queryParam("to", to)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<DocumentMetadata>>() {});
    }

    @Override
    public List<DocumentMetadata> searchByDocumentDate(long from, long to) {
        return ClientBuilder.newClient().target(baseUrl)
                .path("/filter/documentDate")
                .queryParam("from", from)
                .queryParam("to", to)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<DocumentMetadata>>() {});
    }

    @Override
    public DocumentMetadata getById(String uuid) {
        return ClientBuilder.newClient().target(baseUrl)
                .path(uuid)
                .request(MediaType.APPLICATION_JSON)
                .get(DocumentMetadata.class);
    }

    @Override
    public List<DocumentMetadata> findAll() {
        return ClientBuilder.newClient().target(baseUrl)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<DocumentMetadata>>() {});
    }

    @Override
    public boolean delete(String uuid) {
        return ClientBuilder.newClient().target(baseUrl)
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

        return ClientBuilder.newClient().target(baseUrl)
                .register(new BasicAuthentication(fileArchiveManagerUser, fileArchiveManagerPassword))
                .request()
                .post(Entity.entity(new GenericEntity<MultipartFormDataOutput>(form) {}, MediaType.MULTIPART_FORM_DATA_TYPE), Boolean.class);

//        HttpClient client = HttpClientBuilder.create().build();
//        HttpEntity multipart = null;
//        try {
//            multipart = MultipartEntityBuilder.create()
//                    .addTextBody("metadata", objectMapper.writeValueAsString(documentMetadata), ContentType.APPLICATION_JSON)
//                    .addBinaryBody("content", content)
//                    .build();
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//        HttpPost post = new HttpPost(baseUrl);
//        post.setHeader("Authorization", "Basic " + new String(Base64.getEncoder().encode((fileArchiveManagerUser + ":" + fileArchiveManagerPassword).getBytes())));
//        post.setEntity(multipart);
//
//        try {
//            HttpResponse response = client.execute(post);
//
//            return objectMapper.readValue(response.getEntity().getContent(), Boolean.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
    }

    @Override
    public boolean setContent(String uuid, InputStream content) {
        MultipartFormDataOutput form = new MultipartFormDataOutput();
        form.addFormData("content", content, MediaType.APPLICATION_OCTET_STREAM_TYPE);

        return ClientBuilder.newClient().target(baseUrl)
                .path(uuid + "/content")
                .register(new BasicAuthentication(fileArchiveManagerUser, fileArchiveManagerPassword))
                .request()
                .post(Entity.entity(new GenericEntity<MultipartFormDataOutput>(form) {}, MediaType.MULTIPART_FORM_DATA_TYPE), Boolean.class);
    }

    @Override
    public long getContentLength(String uuid) {
        Response response = ClientBuilder.newClient().target(baseUrl)
                .path(uuid + "/content")
                .request()
                .head();

        String length = response.getHeaderString("Content-Length");

        if (length != null) {
            return Long.valueOf(length);
        }

        return -1;
    }

    @Override
    public boolean exists(String uuid) {
        Response response = ClientBuilder.newClient().target(baseUrl)
                .path(uuid + "/content")
                .request()
                .head();

        return response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL;
    }

    @Override
    public InputStream getContentAsStream(String uuid) {
        final Client client = ClientBuilder.newClient();
        InputStream contentStream = client.target(baseUrl)
                .path(uuid + "/content")
                .request()
                .accept(MediaType.APPLICATION_OCTET_STREAM_TYPE)
                .get(new GenericType<InputStream>() {
                });

        return new FilterInputStream(contentStream) {
            // keeps content input stream alive before client is GC'ed
            private Client clientReference = client;

            @Override
            public int read() throws IOException {
                return super.read();
            }
        };
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setFileArchiveManagerUser(String fileArchiveManagerUser) {
        this.fileArchiveManagerUser = fileArchiveManagerUser;
    }

    public void setFileArchiveManagerPassword(String fileArchiveManagerPassword) {
        this.fileArchiveManagerPassword = fileArchiveManagerPassword;
    }

}

