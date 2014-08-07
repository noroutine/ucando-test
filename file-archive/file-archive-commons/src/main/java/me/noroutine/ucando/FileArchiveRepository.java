package me.noroutine.ucando;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by oleksii on 07/08/14.
 */
public interface FileArchiveRepository {

    void upload(Document document);

    List<Document> searchByUploader(String uploader);

    List<Document> searchByUploadedTime(Date from, Date to);

    Document getById(UUID uuid);
}
