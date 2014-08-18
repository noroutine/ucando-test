package me.noroutine.ucando;


import java.util.List;

/**
 * Created by oleksii on 16/08/14.
 */
public interface DocumentMetadataRepository {

    boolean create(DocumentMetadata metadata);

    boolean delete(String uuid);

    List<DocumentMetadata> searchByUploader(String uploader);

    List<DocumentMetadata> searchByDocumentDate(long from, long to);

    List<DocumentMetadata> searchByUploadTime(long from, long to);

    DocumentMetadata getById(String uuid);

    List<DocumentMetadata> findAll();

    boolean exists(String uuid);
}
