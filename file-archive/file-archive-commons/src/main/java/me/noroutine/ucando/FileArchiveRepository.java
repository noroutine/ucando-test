package me.noroutine.ucando;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by oleksii on 07/08/14.
 */
public interface FileArchiveRepository {

    void upload(DocumentMetadata documentMetadata);

    List<DocumentMetadata> searchByUploader(String uploader);

    List<DocumentMetadata> searchByUploadedTime(Date from, Date to);

    DocumentMetadata getById(String uuid);

    List<DocumentMetadata> findAll();

    void delete(String uuid);

    byte[] getContent(String uuid);
}
