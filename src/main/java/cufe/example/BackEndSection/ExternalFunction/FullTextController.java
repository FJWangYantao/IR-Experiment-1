package cufe.example.BackEndSection.ExternalFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FullTextController {
    @Autowired
    private FullTextService fullTextService;

    @GetMapping("/fulltext")
    public FullTextEntity getByTitle(@RequestParam String title){
      FullTextEntity fullTextEntity =  fullTextService.findByTitle(title);
      return fullTextEntity;
    }


}
