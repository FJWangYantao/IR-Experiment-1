package cufe.example.BackEndSection;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.store.Directory;
import java.util.ArrayList;
import java.util.List;

public class SearchService {

    private final StandardAnalyzer analyzer;
    private final Directory index;

    public SearchService(StandardAnalyzer analyzer, Directory index) {
        this.analyzer = analyzer;
        this.index = index;
    }

    public List<SearchResult> search(String field, String queryStr, int hitsPerPage) throws Exception {
        Query query = new QueryParser(field, analyzer).parse(queryStr);

        try (var reader = DirectoryReader.open(index)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs docs = searcher.search(query, hitsPerPage);
            Highlighter highlighter = new Highlighter(new SimpleHTMLFormatter("<b>", "</b>"), new QueryScorer(query));

            List<SearchResult> results = new ArrayList<>();

            for (ScoreDoc hit : docs.scoreDocs) {
                Document doc = searcher.doc(hit.doc);
                String uuid = doc.get("uuid");
                String text = doc.get(field);
                String highlightedText = highlighter.getBestFragment(analyzer, field, text);
                SearchResult searchResult = new SearchResult(doc.get("title"), doc.get("author"), highlightedText, uuid);
                results.add(searchResult);
            }
            return results;
        }
    }
}
