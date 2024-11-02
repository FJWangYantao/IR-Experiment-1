package cufe.example;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class Indexer {
    private Directory index = new RAMDirectory();
    private StandardAnalyzer analyzer = new StandardAnalyzer();

    public void indexDocument(String title, String authors, String date,
                              String affiliation, String address, String fulltext) throws Exception {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        try (IndexWriter writer = new IndexWriter(index, config)) {
            //文档检索字段
            Document doc = new Document();
            doc.add(new TextField("title", title, Field.Store.YES));
            doc.add(new TextField("authors", authors, Field.Store.YES));
            doc.add(new StringField("date", date, Field.Store.YES));
            doc.add(new TextField("affiliation", affiliation, Field.Store.YES));
            doc.add(new TextField("address", address, Field.Store.YES));
            doc.add(new TextField("fulltext", fulltext, Field.Store.YES));
            writer.addDocument(doc);
        }
    }

    public void search(String field, String queryStr) throws Exception {
        Query query = new QueryParser(field, analyzer).parse(queryStr);
        try (DirectoryReader reader = DirectoryReader.open(index)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs results = searcher.search(query, 10);
            System.out.println("Total Hits: " + results.totalHits);

            for (var hit : results.scoreDocs) {
                Document doc = searcher.doc(hit.doc);
                System.out.println("Title: " + doc.get("title"));
            }
        }
    }
}
