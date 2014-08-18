package me.noroutine.ucando;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.noroutine.ucando.types.OperationResult;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
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

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(method = RequestMethod.GET)
    public String filesHome() {
        return "view.documents";
    }

    @RequestMapping(value = "/all", produces = MediaType.APPLICATION_JSON, method = RequestMethod.GET)
    @ResponseBody
    public List<DocumentMetadata> getDocuments() {
        return fileArchiveRepository.findAll();
    }

    @RequestMapping(value = "/filter/uploadedBy", produces = MediaType.APPLICATION_JSON, method = RequestMethod.GET)
    @ResponseBody
    public List<DocumentMetadata> getDocumentsFilteredByName(@RequestParam String uploadedBy) {
        return fileArchiveRepository.searchByUploader(uploadedBy);
    }

    @RequestMapping(value = "/filter/uploadedTime", produces = MediaType.APPLICATION_JSON, method = RequestMethod.GET)
    @ResponseBody
    public List<DocumentMetadata> getDocumentsFilteredByTime(@RequestParam long from, @RequestParam long to) {
        return fileArchiveRepository.searchByUploadedTime(from, to);
    }

    @RequestMapping(value = "/filter/documentDate", produces = MediaType.APPLICATION_JSON, method = RequestMethod.GET)
    @ResponseBody
    public List<DocumentMetadata> getDocumentsFilteredByDocumentDate(@RequestParam long from, @RequestParam long to) {
        return fileArchiveRepository.searchByDocumentDate(from, to);
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
    @ResponseBody
    public OperationResult delete(@PathVariable String uuid) {
        fileArchiveRepository.delete(uuid);
        return OperationResult.ok();
    }

    @RequestMapping(value = "/{uuid}/download/{fileName}")
    public void download(@PathVariable String uuid, HttpServletResponse response) {
        try {
            DocumentMetadata documentMetadata = fileArchiveRepository.getById(uuid);
            if (documentMetadata != null) {
                response.setHeader("Content-Length", Long.toString(documentMetadata.getContentLength()));
                response.setHeader("Content-Type", documentMetadata.getContentType());
                response.setStatus(200);
                IOUtils.copy(fileArchiveRepository.getContentAsStream(uuid), response.getOutputStream());
            }
        } catch (IOException e) {
            response.setStatus(500);
        }
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA)
    @ResponseBody
    public OperationResult uploadFile(HttpServletRequest request, Principal principal) {
        User activeUser = (User) ((Authentication) principal).getPrincipal();

        ServletFileUpload upload = new ServletFileUpload();
        FileItemIterator itemIterator;
        DocumentMetadata submittedMetadata = null;
        InputStream submittedContent = null;

        try {
            itemIterator = upload.getItemIterator(request);
            FileItemStream item = itemIterator.next();

            if ("metadata".equals(item.getFieldName())) {
                submittedMetadata = objectMapper.readValue(item.openStream(), DocumentMetadata.class);
            } else {
                return OperationResult.fail();
            }

            item = itemIterator.next();
            if ("content".equals(item.getFieldName())) {
                submittedContent = item.openStream();
            } else {
                return OperationResult.fail();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return OperationResult.fail();
        }

        if (submittedContent == null || submittedMetadata == null) {
            return OperationResult.fail();
        }

        DocumentMetadata documentMetadata = new DocumentMetadata();
        documentMetadata.setUuid(UUID.randomUUID().toString());

        documentMetadata.setFileName(submittedMetadata.getFileName());
        documentMetadata.setDocumentDate(submittedMetadata.getDocumentDate());
        documentMetadata.setUploadTime(submittedMetadata.getUploadTime());

        documentMetadata.setUploadedBy(activeUser.getUsername());

        fileArchiveRepository.createDocument(documentMetadata, submittedContent);
        return OperationResult.ok();
    }
}
