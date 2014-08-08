package me.noroutine.ucando;

import me.noroutine.ucando.types.OperationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

/**
 * Created by oleksii on 07/08/14.
 */
@Controller
@RequestMapping("/documents")
public class DocumentsController {

    @Autowired
    private FileArchiveRepository fileArchiveRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String filesHome() {
        return "view.documents";
    }

    @RequestMapping(value = "/all", produces = MediaType.APPLICATION_JSON, method = RequestMethod.GET)
    @ResponseBody
    public List<DocumentMetadata> getDocuments() {
        return fileArchiveRepository.findAll();
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
    @ResponseBody
    public OperationResult delete(@PathVariable String uuid) {
        fileArchiveRepository.delete(uuid);
        return OperationResult.ok();
    }

    @RequestMapping(value = "/{uuid}/download")
    @ResponseBody
    public byte[] download(String uuid) {
        return fileArchiveRepository.getContent(uuid);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public OperationResult uploadFile(
            @RequestPart MultipartFile content,
            @RequestPart DocumentMetadata metadata,
            Principal principal) {

        User activeUser = (User) ((Authentication) principal).getPrincipal();

        DocumentMetadata documentMetadata = new DocumentMetadata();
        documentMetadata.setUuid(UUID.randomUUID().toString());

        documentMetadata.setFileName(metadata.getFileName());
        documentMetadata.setDocumentDate(metadata.getDocumentDate());
        documentMetadata.setUploadTime(metadata.getUploadTime());

        documentMetadata.setUploadedBy(activeUser.getUsername());

        try {
            fileArchiveRepository.createDocument(documentMetadata, content.getInputStream());
            return OperationResult.ok();
        } catch (IOException e) {
            return OperationResult.fail();
        }
    }
}
