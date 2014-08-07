package me.noroutine.ucando;

import java.util.List;

/**
 * Created by oleksii on 07/08/14.
 */
public interface FileArchiveClient {

    void upload(Document document);

    List<Document> search(String keyword);
}
