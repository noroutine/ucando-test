package me.noroutine.ucando;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import java.util.*;

/**
 * Root resource (exposed at "document" path)
 */
@Path("document")
public class FileArchiveService implements FileArchiveRepository {

    @Context
    private Request request;

    private List<Document> documentRepository = new ArrayList<>();

    public FileArchiveService() {
        Document doc = new Document();
        doc.setFileName("file1.xml");
        this.documentRepository.add(doc);
        doc = new Document();
        doc.setFileName("movie2.avi");
        this.documentRepository.add(doc);
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public void upload(Document document) {
        documentRepository.add(document);
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Document> findAll() {
        return Collections.unmodifiableList(documentRepository);
    }

    @GET
    @Path("{uuid}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Document getById(@PathParam("uuid") UUID uuid) {
        return new Document();
    }

    @GET
    @Path("filter/uploadedBy")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Document> searchByUploader(@QueryParam("uploadedBy") String uploadedBy) {
        return Collections.unmodifiableList(documentRepository);
    }

    @GET
    @Path("filter/uploadedTime")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Document> searchByUploadedTime(@QueryParam("from") Date from, @QueryParam("to") Date to) {
        return Collections.unmodifiableList(documentRepository);
    }

}
