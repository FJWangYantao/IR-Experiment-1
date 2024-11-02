package cufe.example.ProcessDataSection;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class FullTextExtractor {

    private final String grobidUrl;

    public FullTextExtractor(String grobidUrl) {
        this.grobidUrl = grobidUrl;
    }

    public String processFulltextDocument(String filePath) throws IOException {
        File file = new File(filePath);
        HttpURLConnection connection = (HttpURLConnection) new URL(grobidUrl + "/api/processFulltextDocument").openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=*****");

        try (DataOutputStream request = new DataOutputStream(connection.getOutputStream())) {
            request.writeBytes("--*****\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"input\"; filename=\"" + file.getName() + "\"\r\n");
            request.writeBytes("\r\n");

            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    request.write(buffer, 0, bytesRead);
                }
            }

            request.writeBytes("\r\n");
            request.writeBytes("--*****--\r\n");
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                return response.toString();
            }
        } else {
            throw new IOException("Grobid request failed. Response code: " + responseCode);
        }
    }

    public void saveToXmlFile(String content, String outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write(content);
        }
    }

    public static void main(String[] args) {
        FullTextExtractor client = new FullTextExtractor("http://localhost:8070");
        try {
            String pdfFullTextXml;
            int count = 1;
            File filePath = new File("D:/360MoveData/Users/asus/Desktop/oriPDFs/oriPDFs/");
            for(File pdfFile : filePath.listFiles()){
                if (pdfFile.isFile() && pdfFile.getName().endsWith(".pdf")){
                    pdfFullTextXml = client.processFulltextDocument(pdfFile.getPath());
                    client.saveToXmlFile(pdfFullTextXml,"D:/360MoveData/Users/asus/Desktop/XML/FullTexts/" + "fullTexts" + count + ".xml");
                    System.out.println("Full Text " + count + " was extracted successfully.");
                    count++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
