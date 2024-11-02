package cufe.example;

import org.jbibtex.*;

import java.io.*;
import java.util.Map;

public class BibTeXToXMLConverter {

    public static void main(String[] args) {
        try {
            int count = 1;
            // 读取 BibTeX 文件
            File filePath = new File("D:/360MoveData/Users/asus/Desktop/XML/Metadata/");
            for (File bibTexFile : filePath.listFiles()){
                StringBuilder convertedContent = new StringBuilder();
                if (bibTexFile.isFile() && bibTexFile.getName().endsWith(".bib")){
                    // 使用 JBibTeX 解析器解析 BibTeX
                    FileReader parsedFile = new FileReader(bibTexFile);
                    BibTeXParser parser = new BibTeXParser();
                    BibTeXDatabase database = parser.parse(parsedFile);
                    convertedContent.append("<entry>\n");

                    // 将 BibTeX 条目转换为 XML
                    for (Map.Entry<Key, BibTeXEntry> entry : database.getEntries().entrySet()) {
                        BibTeXEntry bibEntry = entry.getValue();
                        bibEntry.getFields().forEach((key, value) -> {
                            convertedContent.append("  <" + key.getValue() + ">" + value.toUserString() + "</" + key.getValue() + ">\n");
                        });
                    }
                    convertedContent.append("</entry>\n");
                    String xmlFormatFile = escapeXml(convertedContent.toString());
                    saveToXmlFile(xmlFormatFile,"D:/360MoveData/Users/asus/Desktop/XML/ConvertedData/" + "convertedData_" + count + ".xml");
                    System.out.println("The file" + count + "has been converted.");
                    count++;
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
    public static void saveToXmlFile(String content, String outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write(content);
        }
    }
    public static String escapeXml(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("&", "&amp;");
    }

}
