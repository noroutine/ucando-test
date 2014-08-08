package me.noroutine.ucando;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by oleksii on 07/08/14.
 */
public interface FileArchiveRepository {

    boolean createDocument(DocumentMetadata documentMetadata);

    boolean createDocument(DocumentMetadata documentMetadata, InputStream content);

    List<DocumentMetadata> searchByUploader(String uploader);

    List<DocumentMetadata> searchByUploadedTime(Date from, Date to);

    DocumentMetadata getById(String uuid);

    List<DocumentMetadata> findAll();

    boolean delete(String uuid);

    byte[] getContent(String uuid);

    boolean setContent(String uuid, InputStream input);
}
