package me.noroutine.ucando;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by oleksii on 07/08/14.
 */
@Controller
@RequestMapping("/documents")
public class FilesController {

    @Autowired
    private FileArchiveRepository fileArchiveRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String filesHome(Model model) {
        model.addAttribute("documents", fileArchiveRepository.searchByUploader(null));
        return "view.documents";
    }

    @RequestMapping(method = RequestMethod.POST)
    public void uploadFile(
//            @RequestPart("metadata") Document metadata,
            @RequestPart("file") MultipartFile file) {

    }
}
