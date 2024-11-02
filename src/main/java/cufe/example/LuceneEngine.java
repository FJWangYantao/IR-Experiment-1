package cufe.example;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class LuceneEngine {
    public static void main(String[] args) throws Exception {

        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory index = new RAMDirectory();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(index, config);

        File folder = new File("D:/360MoveData/Users/asus/Desktop/XML/ConvertedData/");
        File[] listOfFiles = folder.listFiles();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(".xml")) {
                org.w3c.dom.Document xmlDoc = dBuilder.parse(file);
                Document doc = new Document();
                // 检查 "title" 标签是否存在
                if (xmlDoc.getElementsByTagName("title").getLength() > 0) {
                    String title = xmlDoc.getElementsByTagName("title").item(0).getTextContent();
                    doc.add(new TextField("title", title, Field.Store.YES));
                }

                // 检查 "author" 标签是否存在
                if (xmlDoc.getElementsByTagName("author").getLength() > 0) {
                    String authors = xmlDoc.getElementsByTagName("author").item(0).getTextContent();
                    doc.add(new TextField("author", authors, Field.Store.YES));
                }

                // 检查 "date" 标签是否存在
                if (xmlDoc.getElementsByTagName("date").getLength() > 0) {
                    String date = xmlDoc.getElementsByTagName("date").item(0).getTextContent();
                    doc.add(new TextField("date", date, Field.Store.YES));
                }

                // 检查 "affiliation" 标签是否存在
                if (xmlDoc.getElementsByTagName("affiliation").getLength() > 0) {
                    String affiliation = xmlDoc.getElementsByTagName("affiliation").item(0).getTextContent();
                    doc.add(new TextField("affiliation", affiliation, Field.Store.YES));
                }

                // 检查 "address" 标签是否存在
                if (xmlDoc.getElementsByTagName("address").getLength() > 0) {
                    String address = xmlDoc.getElementsByTagName("address").item(0).getTextContent();
                    doc.add(new TextField("address", address, Field.Store.YES));
                }

                //keywords字段
                if (xmlDoc.getElementsByTagName("keywords").getLength() > 0) {
                    String keywords = xmlDoc.getElementsByTagName("keywords").item(0).getTextContent();
                    doc.add(new TextField("keywords", keywords, Field.Store.YES));
                }

                writer.addDocument(doc);
            }
        }

        writer.close();

        // Example of querying the index
        String querystr = "3D";
        Query q = new QueryParser("keywords", analyzer).parse(querystr);

        // Search
        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;

        // Display results
        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("title"));
        }

        // Close the reader
        reader.close();
    }
}
