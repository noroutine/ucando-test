package me.noroutine.ucando;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleksii on 08/08/14.
 */
public enum Storage {
    instance;

    private List<DocumentMetadata> documentMetadataRepository = new ArrayList<>();

    public List<DocumentMetadata> getDocumentMetadataRepository() {
        return documentMetadataRepository;
    }
}
