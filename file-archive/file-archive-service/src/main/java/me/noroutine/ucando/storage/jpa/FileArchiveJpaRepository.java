package me.noroutine.ucando.storage.jpa;

import me.noroutine.ucando.DocumentMetadata;
import me.noroutine.ucando.FileArchiveRepository;
import me.noroutine.ucando.storage.jpa.annotation.JpaBacked;
import me.noroutine.ucando.storage.jpa.orm.DocumentContentMapping;
import me.noroutine.ucando.storage.jpa.orm.DocumentMetadataMapping;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by oleksii on 14/08/14.
 */
@JpaBacked
@ApplicationScoped
public class FileArchiveJpaRepository implements FileArchiveRepository {

    @PersistenceContext(unitName = "FileArchivePU")
    private EntityManager entityManager;

    @Override
    @Transactional
    public boolean createDocument(DocumentMetadata documentMetadata) {
        return false;
    }

    @Override
    @Transactional
    public boolean createDocument(DocumentMetadata documentMetadata, InputStream content) {
        DocumentMetadataMapping metadataMapping = DocumentMetadataMapping.wrap(documentMetadata);

        Session session = entityManager.unwrap(org.hibernate.Session.class);
        Blob contentBlob = Hibernate.getLobCreator(session).createBlob(content, -1);

        entityManager.persist(metadataMapping);

        entityManager.flush();

        DocumentContentMapping documentContentMapping = entityManager.find(DocumentContentMapping.class, metadataMapping.getUuid());
        documentContentMapping.setContent(contentBlob);
        entityManager.merge(documentContentMapping);

        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DocumentMetadata> searchByUploader(String uploader) {
        return DocumentMetadataMapping.unwrapList(entityManager.createNamedQuery("documents.findByUploader")
                .setParameter("uploadedBy", uploader)
                .getResultList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DocumentMetadata> searchByUploadedTime(long from, long to) {
        return DocumentMetadataMapping.unwrapList(entityManager.createNamedQuery("documents.findByUploadTimeRange")
                .setParameter("from_time", new Date(from))
                .setParameter("to_time", new Date(to))
                .getResultList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DocumentMetadata> searchByDocumentDate(long from, long to) {
        return DocumentMetadataMapping.unwrapList(entityManager.createNamedQuery("documents.findBydocumentDateRange")
                .setParameter("from_time", new Date(from))
                .setParameter("to_time", new Date(to))
                .getResultList());
    }

    @Override
    public DocumentMetadata getById(String uuid) {
        return DocumentMetadataMapping.unwrap(entityManager.find(DocumentMetadataMapping.class, uuid));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DocumentMetadata> findAll() {
        return DocumentMetadataMapping.unwrapList(entityManager.createNamedQuery("documents.findAll")
                .getResultList());
    }

    @Override
    @Transactional
    public boolean delete(String uuid) {
        DocumentMetadataMapping forDeletion = entityManager.find(DocumentMetadataMapping.class, uuid);

        if (forDeletion != null) {
            entityManager.remove(forDeletion);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public InputStream getContentAsStream(String uuid) {
        try {
            return entityManager.find(DocumentContentMapping.class, uuid).getContent().getBinaryStream();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional
    public boolean setContent(String uuid, InputStream input) {
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        Blob contentBlob = Hibernate.getLobCreator(session).createBlob(input, -1);
        DocumentContentMapping documentContentMapping = entityManager.find(DocumentContentMapping.class, uuid);
        documentContentMapping.setContent(contentBlob);
        entityManager.merge(documentContentMapping);
        return true;
    }

}
