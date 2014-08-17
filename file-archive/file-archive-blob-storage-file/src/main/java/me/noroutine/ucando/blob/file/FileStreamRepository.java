package me.noroutine.ucando.blob.file;

import me.noroutine.ucando.StreamRepository;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * Created by oleksii on 17/08/14.
 */
public class FileStreamRepository implements StreamRepository {

    private String storageLocation;

    @Override
    public boolean write(String uuid, InputStream stream) {
        File target = new File(storageLocation, uuid);
        try {
            FileOutputStream out = new FileOutputStream(target);
            IOUtils.copy(stream, out);
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public InputStream read(String uuid) {
        try {
            return new FileInputStream(new File(storageLocation, uuid));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delete(String uuid) {
        File blobFile = new File(storageLocation, uuid);
        return blobFile.exists() && blobFile.delete();
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }
}
