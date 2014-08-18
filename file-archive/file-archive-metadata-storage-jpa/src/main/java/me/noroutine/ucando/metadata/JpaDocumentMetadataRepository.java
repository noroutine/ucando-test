package me.noroutine.ucando.metadata;

import me.noroutine.ucando.DocumentMetadata;
import me.noroutine.ucando.DocumentMetadataRepository;
import me.noroutine.ucando.metadata.orm.DocumentMetadataMapping;

import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Created by oleksii on 16/08/14.
 */

@Singleton
public class JpaDocumentMetadataRepository implements DocumentMetadataRepository {

    @PersistenceContext(unitName = "FileArchivePU")
    private EntityManager entityManager;

    @Override
    @Transactional
    public boolean create(DocumentMetadata metadata) {
        DocumentMetadataMapping metadataMapping = DocumentMetadataMapping.wrap(metadata);
        entityManager.persist(metadataMapping);
        return true;
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
    @SuppressWarnings("unchecked")
    public List<DocumentMetadata> searchByUploader(String uploader) {
        return DocumentMetadataMapping.unwrapList(entityManager.createNamedQuery("documents.findByUploader")
                .setParameter("uploadedBy", uploader)
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
    @SuppressWarnings("unchecked")
    public List<DocumentMetadata> searchByUploadTime(long from, long to) {
        return DocumentMetadataMapping.unwrapList(entityManager.createNamedQuery("documents.findByUploadTimeRange")
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
    public boolean exists(String uuid) {
        return entityManager.find(DocumentMetadataMapping.class, uuid) != null;
    }
}
