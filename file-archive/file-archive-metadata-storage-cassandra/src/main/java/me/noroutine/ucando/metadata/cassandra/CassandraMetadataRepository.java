package me.noroutine.ucando.metadata.cassandra;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import me.noroutine.ucando.DocumentMetadata;
import me.noroutine.ucando.DocumentMetadataRepository;
import me.noroutine.ucando.metadata.cassandra.mapping.DocumentMetadataMapping;
import org.springframework.data.cassandra.core.CassandraOperations;

import java.util.List;
import java.util.UUID;

/**
 * Created by oleksii on 17/08/14.
 */
public class CassandraMetadataRepository implements DocumentMetadataRepository {

    private CassandraOperations cassandraOperations;

    @Override
    public boolean create(DocumentMetadata metadata) {
        cassandraOperations.insert(DocumentMetadataMapping.wrap(metadata));
        return true;
    }

    @Override
    public boolean delete(String uuid) {
        cassandraOperations.deleteById(DocumentMetadataMapping.class, UUID.fromString(uuid));
        return true;
    }

    @Override
    public List<DocumentMetadata> searchByUploader(String uploader) {
        Select select = QueryBuilder.select().from("documents");
        select.where(QueryBuilder.eq("uploaded_by", uploader));

        return DocumentMetadataMapping.unwrapList(cassandraOperations.select(select, DocumentMetadataMapping.class));
    }

    @Override
    public List<DocumentMetadata> searchByDocumentDate(long from, long to) {
        Select select = QueryBuilder.select().from("documents");
        select.where(QueryBuilder.gte("document_date", from))
                .and(QueryBuilder.lte("document_date", to));
        select.allowFiltering();

        return DocumentMetadataMapping.unwrapList(cassandraOperations.select(select, DocumentMetadataMapping.class));
    }

    @Override
    public List<DocumentMetadata> searchByUploadTime(long from, long to) {
        Select select = QueryBuilder.select().from("documents");
        select.where(QueryBuilder.gte("upload_time", from))
                .and(QueryBuilder.lte("upload_time", to));
        select.allowFiltering();

        return DocumentMetadataMapping.unwrapList(cassandraOperations.select(select, DocumentMetadataMapping.class));
    }

    @Override
    public DocumentMetadata getById(String uuid) {
        return DocumentMetadataMapping.unwrap(cassandraOperations.selectOneById(DocumentMetadataMapping.class, UUID.fromString(uuid)));
    }

    @Override
    public List<DocumentMetadata> findAll() {
        return DocumentMetadataMapping.unwrapList(cassandraOperations.selectAll(DocumentMetadataMapping.class));
    }

    public void setCassandraOperations(CassandraOperations cassandraOperations) {
        this.cassandraOperations = cassandraOperations;
    }
}
