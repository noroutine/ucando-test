package me.noroutine.ucando;

import java.io.InputStream;

/**
 * Created by oleksii on 14/08/14.
 */
public interface StreamRepository {

    boolean write(String uuid, InputStream stream);

    InputStream read(String uuid);

    boolean delete(String uuid);

    long getContentLength(String uuid);
}
