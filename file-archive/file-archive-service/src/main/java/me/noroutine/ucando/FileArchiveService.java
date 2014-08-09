package me.noroutine.ucando;


import me.noroutine.ucando.orm.DocumentMetadata;
import org.jboss.resteasy.plugins.providers.multipart.MultipartInput;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import java.util.*;

/**
 * Root resource (exposed at "document" path)
 */
@Path("document")
@RequestScoped
public class FileArchiveService {

    @PersistenceContext(unitName = "FileArchivePU")
    private EntityManager entityManager;

    @Context
    private Request request;

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public List<DocumentMetadata> findAll() {
        return entityManager.createNamedQuery("documents.findAll")
                .getResultList();
    }

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Transactional
    public boolean createDocument(DocumentMetadata documentMetadata) {
        entityManager.persist(documentMetadata);
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
        return entityManager.find(DocumentMetadata.class, uuid);
    }

    @DELETE
    @Path("{uuid}")
    @Produces({ MediaType.APPLICATION_JSON })
    @Transactional
    public boolean delete(@PathParam("uuid") String uuid) {
        DocumentMetadata forDeletion = getById(uuid);

        if (forDeletion != null) {
            entityManager.remove(forDeletion);
            return true;
        } else {
            return false;
        }
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
        return findAll();
    }

    @GET
    @Path("filter/uploadedTime")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<DocumentMetadata> searchByUploadedTime(@QueryParam("from") Date from, @QueryParam("to") Date to) {
        return findAll();
    }

}
