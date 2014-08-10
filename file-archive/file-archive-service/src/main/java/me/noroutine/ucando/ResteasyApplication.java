package me.noroutine.ucando;

import javax.servlet.annotation.MultipartConfig;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Created by oleksii on 07/08/14.
 */
@ApplicationPath("/")
@MultipartConfig(location = "/tmp",
        fileSizeThreshold = 1024 * 1024 * 1024,
        maxFileSize = 1024 * 1024 * 1024, maxRequestSize = 1024 * 1024 * 1024)
public class ResteasyApplication extends Application {

    public ResteasyApplication() {

    }
}
