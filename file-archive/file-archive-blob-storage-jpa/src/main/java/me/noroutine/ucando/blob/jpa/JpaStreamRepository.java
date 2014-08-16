package me.noroutine.ucando.blob.jpa;

import me.noroutine.ucando.StreamRepository;
import me.noroutine.ucando.blob.jpa.orm.DocumentContentMapping;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * Created by oleksii on 14/08/14.
 */

@Singleton
public class JpaStreamRepository implements StreamRepository {

    @PersistenceContext(unitName = "BlobContentPU")
    private EntityManager entityManager;

    @Override
    @Transactional
    public boolean write(String uuid, InputStream stream) {

        Session session = entityManager.unwrap(org.hibernate.Session.class);
        Blob contentBlob = Hibernate.getLobCreator(session).createBlob(stream, -1);

        DocumentContentMapping documentContentMapping = entityManager.find(DocumentContentMapping.class, uuid);

        if (documentContentMapping != null) {
            documentContentMapping.setContent(contentBlob);
            entityManager.merge(documentContentMapping);
        } else {
            documentContentMapping = new DocumentContentMapping();
            documentContentMapping.setUuid(uuid);
            documentContentMapping.setContent(contentBlob);
            entityManager.persist(documentContentMapping);
        }

        return true;
    }

    @Override
    public InputStream read(String uuid) {
        try {
            return entityManager.find(DocumentContentMapping.class, uuid).getContent().getBinaryStream();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional
    public boolean delete(String uuid) {
        DocumentContentMapping documentContentMapping = entityManager.find(DocumentContentMapping.class, uuid);

        if (documentContentMapping != null) {
            entityManager.remove(documentContentMapping);
            return true;
        } else {
            return false;
        }
    }

}
