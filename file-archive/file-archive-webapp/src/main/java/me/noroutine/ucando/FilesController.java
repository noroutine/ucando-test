package me.noroutine.ucando;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by oleksii on 07/08/14.
 */
@Controller
public class FilesController {

    @Autowired
    private FileArchiveRepository fileArchiveRepository;

    @RequestMapping("/documents")
    public String filesHome(Model model) {
        model.addAttribute("documents", fileArchiveRepository.searchByUploader(null));
        return "view.documents";
    }
}
