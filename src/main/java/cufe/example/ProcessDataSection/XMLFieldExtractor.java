package cufe.example.ProcessDataSection;

import org.json.JSONObject;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class XMLFieldExtractor {

    public static void main(String[] args) throws Exception {
        File filePath = new File("D:/360MoveData/Users/asus/Desktop/XML/FullTexts/");

        // 获取所有 XML 文件并按数字顺序排序
        File[] fulltextFiles = filePath.listFiles((dir, name) -> name.endsWith(".xml"));
        Arrays.sort(fulltextFiles, Comparator.comparing(XMLFieldExtractor::extractNumber));

        int count = 1;

        for (File fulltextFile : fulltextFiles) {
            if (fulltextFile.isFile()) {
                File jsonFilePath = new File("D:/360MoveData/Users/asus/Desktop/XML/ExtractedFullText/text_" + count + ".json");
                JSONObject jsonObject = extractFields(fulltextFile);
                try (FileWriter fileWriter = new FileWriter(jsonFilePath)) {
                    fileWriter.write(jsonObject.toString(4));
//                    System.out.println("The file " + fulltextFile + " extracted successfully.");
                    count++;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static JSONObject extractFields(File xmlFile) throws Exception {
        JSONObject jsonObject = new JSONObject();


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);

        // 提取标题
        NodeList titleNodes = doc.getElementsByTagName("title");
        if (titleNodes.getLength() > 0 && !titleNodes.item(0).getTextContent().equals("")) {
            jsonObject.put("title", titleNodes.item(0).getTextContent());
        }
        else if (titleNodes.getLength() > 2 && !titleNodes.item(1).getTextContent().equals("")){
            jsonObject.put("title", titleNodes.item(1).getTextContent());
        }
        else {
            System.out.println(xmlFile.getName() + "may have wrong title.");
            jsonObject.put("title", "66666666666666666666666666666666666666666666666666666666666666");
        }

        // 提取文章
        NodeList textNodes = doc.getElementsByTagName("text");
        if (textNodes.getLength() > 0) {
            jsonObject.put("text", textNodes.item(0).getTextContent());
        }
        return jsonObject;
    }

    // 提取文件名中的数字
    private static int extractNumber(File file) {
        String name = file.getName();
        String numberPart = name.replaceAll("\\D+", ""); // 提取数字部分
        return numberPart.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(numberPart);
    }
}
