package me.noroutine.ucando.storage.jpa.orm;

import javax.persistence.*;
import java.sql.Blob;

/**
 * Created by oleksii on 10/08/14.
 */
@Entity
@Table(name = "documents")
public class DocumentContentMapping {
    private String uuid;

    private Blob content;

    @Id
    @Column(name = "uuid")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Lob
    @Column(name = "content")
    public Blob getContent() {
        return content;
    }

    public void setContent(Blob content) {
        this.content = content;
    }
}
