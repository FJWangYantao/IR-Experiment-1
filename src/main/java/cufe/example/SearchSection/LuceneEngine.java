package cufe.example.SearchSection;


import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class LuceneEngine {
    private final StandardAnalyzer analyzer;
    private final Directory index;

    private static final Map<String, String> pdfFileMap = new HashMap<>();

    public LuceneEngine(StandardAnalyzer analyzer, Directory index) {
        this.analyzer = analyzer;
        this.index = index;


    }

    public void indexDocuments() throws Exception {

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(index, config);


        //文件数据库路径
        File folder = new File("D:/360MoveData/Users/asus/Desktop/XML/TestData/MetaData/");
        File fullTextFolder = new File("D:/360MoveData/Users/asus/Desktop/XML/TestData/FullText/");
        String pdfPath = "D:/360MoveData/Users/asus/Desktop/oriPDFs/oriPDFs/";

        File[] listOfFiles = folder.listFiles();
        File[] listOfFullTextFiles = fullTextFolder.listFiles();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        Map<String, Document> metadataMap = new HashMap<>();



        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(".xml")) {
                try{
                    org.w3c.dom.Document xmlDoc = dBuilder.parse(file);
                    Document metadataDoc = new Document();

                    String title = null;
                    String uuid = extractUuidFromFileName(file.getName());
                    pdfFileMap.put(uuid, pdfPath + file.getName().substring(0, file.getName().indexOf(".")) + ".pdf");

                    metadataDoc.add(new StringField("uuid",uuid,Field.Store.YES));
                    if (xmlDoc.getElementsByTagName("title").getLength() > 0) {
                        title = xmlDoc.getElementsByTagName("title").item(0).getTextContent();
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

                    if (xmlDoc.getElementsByTagName("abstract").getLength() > 0) {
                        String textAbstract = xmlDoc.getElementsByTagName("abstract").item(0).getTextContent();
                        metadataDoc.add(new TextField("abstract", textAbstract, Field.Store.YES));
                    }

                    if (title != null) {
                        metadataMap.put(title, metadataDoc);
                    }
                }
                catch (Exception e){
                    System.out.println(file.getName() + " may have some wrong");
                }

            }
        }

        for (File fullTextFile: listOfFullTextFiles){
            if (fullTextFile.isFile() && fullTextFile.getName().endsWith(".xml")){
                org.w3c.dom.Document fullTextDocFile = dBuilder.parse(fullTextFile);
                Document fullTextDoc = new Document();
                String title = null;

                if (fullTextDocFile.getElementsByTagName("text").getLength() > 0) {
                    String text = fullTextDocFile.getElementsByTagName("text").item(0).getTextContent();
                    fullTextDoc.add(new TextField("text", text, Field.Store.YES));
                }
                if (fullTextDocFile.getElementsByTagName("title").getLength() > 0) {
                    title = fullTextDocFile.getElementsByTagName("title").item(0).getTextContent();
                    fullTextDoc.add(new TextField("title", title, Field.Store.YES));
                }

                // 合并元数据文档
                if (title != null && metadataMap.containsKey(title)) {
                    Document metadataDoc = metadataMap.get(title);
                    for (IndexableField field : metadataDoc.getFields()) {
                        fullTextDoc.add(field);
                    }
                }

                writer.addDocument(fullTextDoc);
            }
        }
        writer.close();
    }

    private String extractUuidFromFileName(String fileName) {
        return fileName.substring(0, fileName.indexOf(".")); // 假设文件名为 UUID.pdf
    }

    public static String getFilePath(String uuid) {
        return pdfFileMap.get(uuid);
    }


    public static void main(String[] args){
            try {
                // 假设你已经有一个索引目录
                Directory index = new RAMDirectory();
                // 创建索引引擎
                LuceneEngine engine = new LuceneEngine(new StandardAnalyzer(), index);
                engine.indexDocuments(); // 索引文档

                // 创建IndexReader和IndexSearcher
                IndexReader reader = DirectoryReader.open(index);
                IndexSearcher searcher = new IndexSearcher(reader);

                // 查询所有文档
                Query query = new TermQuery(new org.apache.lucene.index.Term("date", "2009")); // 查询标题
                for (int i = 0; i < reader.maxDoc(); i++) {
                    Document doc = reader.document(i);
                    String title = doc.get("title");
                    String author = doc.get("author");
                    String date = doc.get("date");

                    // 打印文档信息
                    System.out.println("Title: " + title);
                    System.out.println("Author: " + author);
                    System.out.println("Date: " + date);
                    System.out.println("--------------------------");
                }

                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
