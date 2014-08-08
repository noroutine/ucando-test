package me.noroutine.ucando;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import java.util.*;
import java.util.function.Predicate;

/**
 * Root resource (exposed at "document" path)
 */
@Path("document")
public class FileArchiveService implements FileArchiveRepository {

    @Context
    private Request request;

    private List<DocumentMetadata> documentMetadataRepository = Storage.instance.getDocumentMetadataRepository();

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public void upload(DocumentMetadata documentMetadata) {
        documentMetadataRepository.add(documentMetadata);
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public List<DocumentMetadata> findAll() {
        return Collections.unmodifiableList(documentMetadataRepository);
    }

    @GET
    @Path("{uuid}")
    @Produces({ MediaType.APPLICATION_JSON })
    public DocumentMetadata getById(@PathParam("uuid") String uuid) {
        return new DocumentMetadata();
    }

    @DELETE
    @Path("{uuid}")
    public void delete(@PathParam("uuid") String uuid) {
        boolean removed = false;
        final Iterator<DocumentMetadata> each = documentMetadataRepository.iterator();
        while (each.hasNext()) {
            if (each.next().getUuid().equals(uuid)) {
                each.remove();
                removed = true;
            }
        }
    }

    @GET
    @Path("{uuid}")
    @Produces({ MediaType.APPLICATION_OCTET_STREAM})
    public byte[] getContent(@PathParam("uuid") String uuid) {
        return new byte[0];
    }

    @GET
    @Path("filter/uploadedBy")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<DocumentMetadata> searchByUploader(@QueryParam("uploadedBy") String uploadedBy) {
        return Collections.unmodifiableList(documentMetadataRepository);
    }

    @GET
    @Path("filter/uploadedTime")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<DocumentMetadata> searchByUploadedTime(@QueryParam("from") Date from, @QueryParam("to") Date to) {
        return Collections.unmodifiableList(documentMetadataRepository);
    }

}
