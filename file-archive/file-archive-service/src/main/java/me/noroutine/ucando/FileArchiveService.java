package me.noroutine.ucando;


import me.noroutine.ucando.orm.DocumentContent;
import me.noroutine.ucando.orm.DocumentMetadata;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
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
    @SuppressWarnings("unchecked")
    public List<DocumentMetadata> findAll() {
        return entityManager.createNamedQuery("documents.findAll")
                .getResultList();
    }

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Transactional
    public boolean createDocument(@MultipartForm DocumentForm documentForm) {
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        Blob contentBlob = Hibernate.getLobCreator(session).createBlob(documentForm.getContent(), -1);

        DocumentMetadata documentMetadata = documentForm.getDocumentMetadata();

        entityManager.persist(documentMetadata);

        entityManager.flush();

        DocumentContent documentContent = entityManager.find(DocumentContent.class, documentMetadata.getUuid());
        documentContent.setContent(contentBlob);
        entityManager.merge(documentContent);

        return true;
    }

    @POST
    @Path("{uuid}/content")
    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    @Produces({ MediaType.APPLICATION_JSON })
    @Transactional
    public boolean setContent(@PathParam("uuid") String uuid, MultipartFormDataInput input) {
        Map<String, List<InputPart>> formParts = input.getFormDataMap();
        List<InputPart> inPart = formParts.get("content");
        try {
            InputStream body = inPart.get(0).getBody(InputStream.class, null);
            Session session = entityManager.unwrap(org.hibernate.Session.class);
            Blob contentBlob = Hibernate.getLobCreator(session).createBlob(body, -1);
            DocumentContent documentContent = entityManager.find(DocumentContent.class, uuid);
            documentContent.setContent(contentBlob);
            entityManager.merge(documentContent);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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
    public Response getContent(@PathParam("uuid") String uuid) throws SQLException {
        DocumentContent documentContent = entityManager.find(DocumentContent.class, uuid);
        if (documentContent != null) {
            return Response.ok(documentContent.getContent().getBinaryStream(), MediaType.APPLICATION_OCTET_STREAM_TYPE).build();
        } else {
            return Response.status(404).build();
        }
    }

    @GET
    @Path("filter/uploadedBy")
    @Produces({ MediaType.APPLICATION_JSON })
    @SuppressWarnings("unchecked")
    public List<DocumentMetadata> searchByUploader(@QueryParam("uploadedBy") String uploadedBy) {
        return entityManager.createNamedQuery("documents.findByUploader")
                .setParameter("uploadedBy", uploadedBy)
                .getResultList();
    }

    @GET
    @Path("filter/uploadedTime")
    @Produces({ MediaType.APPLICATION_JSON })
    @SuppressWarnings("unchecked")
    public List<DocumentMetadata> searchByUploadedTime(@QueryParam("from") long from, @QueryParam("to") long to) {
        return entityManager.createNamedQuery("documents.findByUploadTimeRange")
                .setParameter("from_time", new Date(from))
                .setParameter("to_time", new Date(to))
                .getResultList();
    }

}
