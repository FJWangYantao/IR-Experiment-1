package cufe.example.BackEndSection.ExternalFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static cufe.example.SearchSection.LuceneEngine.getFilePath;

@RestController
public class PdfController {

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/pdf/{uuid}")
    public ResponseEntity<Resource> getPdfByUuid(@PathVariable String uuid) {
        String filePath = getFilePath(uuid);

        if (filePath == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Resource resource = resourceLoader.getResource("file:" + filePath);

        if (!resource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(resource);
    }
}
