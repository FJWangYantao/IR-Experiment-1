package cufe.example.BackEndSection.ExternalFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FullTextService {

    @Autowired
    private FullTextRepository fullTextRepository;

    public FullTextEntity findByTitle(String title){
        System.out.println("Searching for title: " + title);
        return fullTextRepository.findByTitle(title);
    }
}
