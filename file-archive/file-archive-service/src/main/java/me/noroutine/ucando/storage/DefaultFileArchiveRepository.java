package me.noroutine.ucando.storage;

import me.noroutine.ucando.DocumentMetadata;
import me.noroutine.ucando.DocumentMetadataRepository;
import me.noroutine.ucando.FileArchiveRepository;
import me.noroutine.ucando.StreamRepository;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;
import java.io.InputStream;
import java.util.List;

/**
 * Created by oleksii on 14/08/14.
 */
public class DefaultFileArchiveRepository implements FileArchiveRepository {

    private FileTypeMap typeMap = new MimetypesFileTypeMap();

    private StreamRepository streamRepository;

    private DocumentMetadataRepository metadataRepository;

    @Override
    public boolean createDocument(DocumentMetadata documentMetadata) {
        return metadataRepository.create(documentMetadata);
    }

    @Override
    public boolean createDocument(DocumentMetadata documentMetadata, InputStream content) {
        return metadataRepository.create(documentMetadata) && streamRepository.write(documentMetadata.getUuid(), content);
    }

    @Override
    public List<DocumentMetadata> searchByUploader(String uploader) {
        return withTypeAndSize(metadataRepository.searchByUploader(uploader));
    }

    @Override
    public List<DocumentMetadata> searchByUploadedTime(long from, long to) {
        return withTypeAndSize(metadataRepository.searchByUploadTime(from, to));
    }

    @Override
    public List<DocumentMetadata> searchByDocumentDate(long from, long to) {
        return withTypeAndSize(metadataRepository.searchByDocumentDate(from, to));
    }

    @Override
    public DocumentMetadata getById(String uuid) {
        return withTypeAndSize(metadataRepository.getById(uuid));
    }

    @Override
    public List<DocumentMetadata> findAll() {
        return withTypeAndSize(metadataRepository.findAll());
    }

    @Override
    public boolean delete(String uuid) {
        return metadataRepository.delete(uuid) && streamRepository.delete(uuid);
    }

    @Override
    public InputStream getContentAsStream(String uuid) {
        return streamRepository.read(uuid);
    }

    @Override
    public boolean setContent(String uuid, InputStream input) {
        return streamRepository.write(uuid, input);
    }

    @Override
    public boolean exists(String uuid) {
        return metadataRepository.exists(uuid);
    }

    @Override
    public long getContentLength(String uuid) {
        return streamRepository.getContentLength(uuid);
    }

    public StreamRepository getStreamRepository() {
        return streamRepository;
    }

    public void setStreamRepository(StreamRepository streamRepository) {
        this.streamRepository = streamRepository;
    }

    public DocumentMetadataRepository getMetadataRepository() {
        return metadataRepository;
    }

    public void setMetadataRepository(DocumentMetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
    }

    private DocumentMetadata withTypeAndSize(DocumentMetadata documentMetadata) {
        if (documentMetadata.getContentType() == null) {
            documentMetadata.setContentType(typeMap.getContentType(documentMetadata.getFileName()));
        }

        if (documentMetadata.getContentLength() == 0 ) {
            documentMetadata.setContentLength(streamRepository.getContentLength(documentMetadata.getUuid()));
        }

        return documentMetadata;
    }

    private List<DocumentMetadata> withTypeAndSize(List<DocumentMetadata> list) {
        for (DocumentMetadata metadata: list) {
            withTypeAndSize(metadata);
        }
        return list;
    }

}
