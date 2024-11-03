package cufe.example.SearchSection;


import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;


public class LuceneEngine {
    private final StandardAnalyzer analyzer;
    private final Directory index;

    public LuceneEngine(StandardAnalyzer analyzer, Directory index) {
        this.analyzer = analyzer;
        this.index = index;
    }

    public void indexDocuments() throws Exception {

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(index, config);

        //文件数据库路径
        File folder = new File("D:/360MoveData/Users/asus/Desktop/XML/ConvertedData/");
        File fullTextFolder = new File("D:/360MoveData/Users/asus/Desktop/XML/FullTexts/");
        File[] listOfFiles = folder.listFiles();
        File[] listOfFullTextFiles = fullTextFolder.listFiles();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        for (File fullTextFile: listOfFullTextFiles){
            if (fullTextFile.isFile() && fullTextFile.getName().endsWith(".xml")){
                org.w3c.dom.Document fullTextDocFile = dBuilder.parse(fullTextFile);
                Document fullTextDoc = new Document();

                if (fullTextDocFile.getElementsByTagName("text").getLength() > 0) {
                    String text = fullTextDocFile.getElementsByTagName("text").item(0).getTextContent();
                    fullTextDoc.add(new TextField("text", text, Field.Store.YES));
                }
                if (fullTextDocFile.getElementsByTagName("title").getLength() > 0) {
                    String title = fullTextDocFile.getElementsByTagName("title").item(0).getTextContent();
                    fullTextDoc.add(new TextField("title", title, Field.Store.YES));
                }

                writer.addDocument(fullTextDoc);
            }

        }

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(".xml")) {
                org.w3c.dom.Document xmlDoc = dBuilder.parse(file);
                Document metadataDoc = new Document();

                if (xmlDoc.getElementsByTagName("title").getLength() > 0) {
                    String title = xmlDoc.getElementsByTagName("title").item(0).getTextContent();
                    metadataDoc.add(new TextField("title", title, Field.Store.YES));
                }

                if (xmlDoc.getElementsByTagName("author").getLength() > 0) {
                    String authors = xmlDoc.getElementsByTagName("author").item(0).getTextContent();
                    metadataDoc.add(new TextField("author", authors, Field.Store.YES));
                }

                if (xmlDoc.getElementsByTagName("date").getLength() > 0) {
                    String date = xmlDoc.getElementsByTagName("date").item(0).getTextContent();
                    metadataDoc.add(new TextField("date", date, Field.Store.YES));
                }

                if (xmlDoc.getElementsByTagName("affiliation").getLength() > 0) {
                    String affiliation = xmlDoc.getElementsByTagName("affiliation").item(0).getTextContent();
                    metadataDoc.add(new TextField("affiliation", affiliation, Field.Store.YES));
                }

                if (xmlDoc.getElementsByTagName("address").getLength() > 0) {
                    String address = xmlDoc.getElementsByTagName("address").item(0).getTextContent();
                    metadataDoc.add(new TextField("address", address, Field.Store.YES));
                }


                if (xmlDoc.getElementsByTagName("keywords").getLength() > 0) {
                    String keywords = xmlDoc.getElementsByTagName("keywords").item(0).getTextContent();
                    metadataDoc.add(new TextField("keywords", keywords, Field.Store.YES));
                }
                writer.addDocument(metadataDoc);
            }
        }

        writer.close();
    }
}
