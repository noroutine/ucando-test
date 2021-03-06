package me.noroutine.ucando.storage;


import me.noroutine.ucando.DocumentMetadata;
import me.noroutine.ucando.FileArchiveRepository;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.util.GenericType;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Root resource (exposed at "document" path)
 */
@Path("document")
@RequestScoped
public class FileArchiveService {

    private FileArchiveRepository fileArchiveRepository;

    /**
     * Get metadata of stored documents.
     *
     * @return list of metadata entries
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public List<DocumentMetadata> findAll() {
        return fileArchiveRepository.findAll();
    }

    /**
     * Create document entry.
     *
     * Handles submitted multipart/form-data form and creates document with given metadata and content
     * Form parts required with their respective MIME type
     *  metadata            application/json
     *  content             application/octet-stream
     *
     * @param documentForm form with metadata and content
     * @return true if success, false otherwise
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public boolean createDocument(MultipartFormDataInput documentForm) {

        try {
            DocumentMetadata metadata = documentForm.getFormDataPart("metadata", new GenericType<DocumentMetadata>() {});
            InputStream contentStream = documentForm.getFormDataPart("content", new GenericType<InputStream>() {});
            return fileArchiveRepository.createDocument(metadata, contentStream);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Update existing document with new content.
     *
     * Form parts required with their respective MIME type
     *  content             application/octet-stream
     *
     * @param uuid  id of the document
     * @param input form with content
     * @return true if update was successful, false otherwise
     */
    @POST
    @Path("{uuid}/content")
    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    @Produces({ MediaType.APPLICATION_JSON })
    public boolean setContent(@PathParam("uuid") String uuid, MultipartFormDataInput input) {
        Map<String, List<InputPart>> formParts = input.getFormDataMap();
        List<InputPart> inPart = formParts.get("content");
        try {
            InputStream body = inPart.get(0).getBody(InputStream.class, null);
            return fileArchiveRepository.setContent(uuid, body);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get document metadata.
     *
     * @param uuid  document id
     * @return document metadata
     */
    @GET
    @Path("{uuid}")
    @Produces({ MediaType.APPLICATION_JSON })
    public DocumentMetadata getById(@PathParam("uuid") String uuid) {
        return fileArchiveRepository.getById(uuid);
    }

    /**
     * Delete document.
     *
     * @param uuid  document id
     * @return true if document was deleted, false if document was not found
     */
    @DELETE
    @Path("{uuid}")
    @Produces({ MediaType.APPLICATION_JSON })
    public boolean delete(@PathParam("uuid") String uuid) {
        return fileArchiveRepository.delete(uuid);
    }

    /**
     * Fetch the content of stored document.
     *
     * @param uuid  document id
     * @return binary content streamed to client
     */
    @GET
    @Path("{uuid}/content")
    @Produces({ MediaType.APPLICATION_OCTET_STREAM })
    public Response getContent(@PathParam("uuid") String uuid) {
        InputStream stream = fileArchiveRepository.getContentAsStream(uuid);
        if (stream != null) {
            return Response.ok(stream, MediaType.APPLICATION_OCTET_STREAM_TYPE).build();
        } else {
            return Response.status(404).build();
        }
    }

    /**
     * Get content info, specifically length
     *
     * @param uuid  document id
     * @return binary content streamed to client
     */
    @HEAD
    @Path("{uuid}/content")
    public Response getContentLength(@PathParam("uuid") String uuid) {
        if (fileArchiveRepository.exists(uuid)) {
            long length = fileArchiveRepository.getContentLength(uuid);

            if (length != -1) {
                return Response.ok().header("Content-Length", Long.toString(length)).build();
            } else {
                return Response.ok().build();
            }
        } else {
            return Response.status(404).build();
        }
    }

    /**
     * Search documents uploaded by given person.
     *
     * @param uploadedBy    username of person
     * @return  list of found documents, that were uploaded by requested person
     */
    @GET
    @Path("filter/uploadedBy")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<DocumentMetadata> searchByUploader(@QueryParam("uploadedBy") String uploadedBy) {
        return fileArchiveRepository.searchByUploader(uploadedBy);
    }

    /**
     * Search documents uploaded within given time window.
     *
     * Parameters are UNIX-style timestamps
     *
     * @param from  starting timestamp of time window
     * @param to    end timestamp of time window
     * @return      list of found documents, that were uploaded within given time frame
     */
    @GET
    @Path("filter/uploadedTime")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<DocumentMetadata> searchByUploadedTime(@QueryParam("from") long from, @QueryParam("to") long to) {
        return fileArchiveRepository.searchByUploadedTime(from, to);

    }

    /**
     * Search documents modified within given time window.
     *
     * Parameters are UNIX-style timestamps
     *
     * @param from  starting timestamp of time window
     * @param to    end timestamp of time window
     * @return      list of found documents, that have document date within given time frame
     */
    @GET
    @Path("filter/documentDate")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<DocumentMetadata> searchByDocumentdate(@QueryParam("from") long from, @QueryParam("to") long to) {
        return fileArchiveRepository.searchByDocumentDate(from, to);
    }

    public void setFileArchiveRepository(FileArchiveRepository fileArchiveRepository) {
        this.fileArchiveRepository = fileArchiveRepository;
    }
}
