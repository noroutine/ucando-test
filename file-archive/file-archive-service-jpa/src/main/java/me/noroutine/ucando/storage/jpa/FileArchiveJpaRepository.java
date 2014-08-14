package me.noroutine.ucando.storage.jpa;

import me.noroutine.ucando.DocumentMetadata;
import me.noroutine.ucando.FileArchiveRepository;
import me.noroutine.ucando.StreamRepository;
import me.noroutine.ucando.storage.jpa.annotation.JpaBacked;
import me.noroutine.ucando.storage.jpa.orm.DocumentMetadataMapping;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.InputStream;
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

    @Inject
    @Default
    private StreamRepository streamRepository;

    @Override
    @Transactional
    public boolean createDocument(DocumentMetadata documentMetadata) {
        return false;
    }

    @Override
    @Transactional
    public boolean createDocument(DocumentMetadata documentMetadata, InputStream content) {
        DocumentMetadataMapping metadataMapping = DocumentMetadataMapping.wrap(documentMetadata);

        entityManager.persist(metadataMapping);

        entityManager.flush();

        streamRepository.write(documentMetadata.getUuid(), content);

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

            streamRepository.delete(uuid);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public InputStream getContentAsStream(String uuid) {
        return streamRepository.read(uuid);
    }

    @Override
    @Transactional
    public boolean setContent(String uuid, InputStream input) {
        return streamRepository.write(uuid, input);
    }

}
