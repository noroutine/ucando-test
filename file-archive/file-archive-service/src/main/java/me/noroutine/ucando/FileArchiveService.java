package me.noroutine.ucando;


import org.jboss.resteasy.plugins.providers.multipart.MultipartInput;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import java.util.*;

/**
 * Root resource (exposed at "document" path)
 */
@Path("document")
public class FileArchiveService {

    @Context
    private Request request;

    private List<DocumentMetadata> documentMetadataRepository = Storage.instance.getDocumentMetadataRepository();

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public List<DocumentMetadata> findAll() {
        return Collections.unmodifiableList(documentMetadataRepository);
    }

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public boolean createDocument(DocumentMetadata documentMetadata) {
        documentMetadataRepository.add(documentMetadata);
        return true;
    }

    @POST
    @Path("{uuid}/content")
    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    @Produces({ MediaType.APPLICATION_JSON })
    public boolean setContent(@PathParam("uuid") String uuid, MultipartInput input) {
        return true;
    }

    @GET
    @Path("{uuid}")
    @Produces({ MediaType.APPLICATION_JSON })
    public DocumentMetadata getById(@PathParam("uuid") String uuid) {
        return new DocumentMetadata();
    }

    @DELETE
    @Path("{uuid}")
    @Produces({ MediaType.APPLICATION_JSON })
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
    @Produces({ MediaType.APPLICATION_OCTET_STREAM })
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
