package me.noroutine.ucando;

import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import java.io.InputStream;
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

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public List<DocumentMetadata> findAll() {
        return Collections.unmodifiableList(documentMetadataRepository);
    }

    @POST
    public boolean createDocument(DocumentMetadata documentMetadata) {
        documentMetadataRepository.add(documentMetadata);
        return true;
    }

    @Override
    public boolean createDocument(DocumentMetadata documentMetadata, InputStream content) {
        throw new UnsupportedOperationException("Metadata and content should be uploaded separately with this service");
    }

//    @POST
//    @Path("{uuid}/content")
//    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    public boolean setContent(@PathParam("uuid") String uuid, @FormDataParam("content") InputStream input) {
        return true;
    }


    @GET
    @Path("{uuid}")
    public DocumentMetadata getById(@PathParam("uuid") String uuid) {
        return new DocumentMetadata();
    }

    @DELETE
    @Path("{uuid}")
    public boolean delete(@PathParam("uuid") String uuid) {
        boolean removed = false;
        final Iterator<DocumentMetadata> each = documentMetadataRepository.iterator();
        while (each.hasNext()) {
            if (each.next().getUuid().equals(uuid)) {
                each.remove();
                removed = true;
            }
        }

        return removed;
    }

    @GET
    @Path("{uuid}/content")
    @Produces({ MediaType.APPLICATION_OCTET_STREAM})
    public byte[] getContent(@PathParam("uuid") String uuid) {
        return new byte[0];
    }

    @GET
    @Path("filter/uploadedBy")
    public List<DocumentMetadata> searchByUploader(@QueryParam("uploadedBy") String uploadedBy) {
        return Collections.unmodifiableList(documentMetadataRepository);
    }

    @GET
    @Path("filter/uploadedTime")
    public List<DocumentMetadata> searchByUploadedTime(@QueryParam("from") Date from, @QueryParam("to") Date to) {
        return Collections.unmodifiableList(documentMetadataRepository);
    }

}
