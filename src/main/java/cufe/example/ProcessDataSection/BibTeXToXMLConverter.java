package cufe.example.ProcessDataSection;

import org.jbibtex.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

public class BibTeXToXMLConverter {


    public static void main(String[] args) {
        try {
            int count = 1;

            // 读取 BibTeX 文件
            File filePath = new File("D:/360MoveData/Users/asus/Desktop/XML/Metadata/");
            File[] files = filePath.listFiles((dir, name) -> name.endsWith(".bib")); // 只筛选出 .bib 文件

            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File f1, File f2) {
                    // 提取数字部分进行比较
                    int num1 = extractNumber(f1.getName());
                    int num2 = extractNumber(f2.getName());
                    return Integer.compare(num1, num2);
                }

                // 提取文件名中的数字
                private int extractNumber(String name) {
                    try {
                        String num = name.replaceAll("\\D+", "");
                        return Integer.parseInt(num);
                    } catch (NumberFormatException e) {
                        return 0; // 如果没有数字，则返回0
                    }
                }
            });

            for (File bibTexFile : files){
                System.out.println("Processing file: " + bibTexFile.getName());
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
                            convertedContent.append("  <" + key.getValue() + ">" + escapeXml(value.toUserString()) + "</" + key.getValue() + ">\n");
                        });
                    }
                    convertedContent.append("</entry>\n");
//                    String xmlFormatFile = escapeXml(convertedContent.toString());

                    saveToXmlFile(convertedContent.toString(),"D:/360MoveData/Users/asus/Desktop/XML/ConvertedData/" + "convertedData_" + count + ".xml");
                    System.out.println(bibTexFile.getName() + "converted!");
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
        input = input.replace("&", "&amp;");
        input = input.replace("<","&lt;");
        input = input.replace(">","&gt;");
        input = input.replaceAll("[^\\p{Print}]", "");
        return input;
    }




}


