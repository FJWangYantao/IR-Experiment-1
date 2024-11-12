package cufe.example.BackEndSection;

import cufe.example.SearchSection.LuceneEngine;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SearchController {

    private final SearchService searchService;

    public SearchController() throws Exception {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory index = new RAMDirectory();

        // Initialize LuceneEngine and index documents here
        LuceneEngine luceneEngine = new LuceneEngine(analyzer, index);
        luceneEngine.indexDocuments();

        this.searchService = new SearchService(analyzer, index);
    }

    @GetMapping("/search")
    public List<SearchResult> search(@RequestParam String field, @RequestParam String query) throws Exception {
        List<SearchResult> results = searchService.search(field, query, 10);
        return results;
    }



}
