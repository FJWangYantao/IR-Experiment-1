package cufe.example;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class MetaDataExtractor {

    private final String grobidUrl;

    public MetaDataExtractor(String grobidUrl) {
        this.grobidUrl = grobidUrl;
    }

    public String ExtractDocumentsMetadata(String filePath) throws IOException {
        File file = new File(filePath);
        HttpURLConnection connection = (HttpURLConnection) new URL(grobidUrl + "/api/processHeaderDocument").openConnection();
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
        MetaDataExtractor client = new MetaDataExtractor("http://localhost:8070");
        try {
            String pdfMetadataXml;
            int count = 1;
            File filePath = new File("D:/360MoveData/Users/asus/Desktop/oriPDFs/oriPDFs/");
            for (File pdfFile : filePath.listFiles()){
                if(count < 718){
                    count++;
                    continue;
                }
                if (pdfFile.isFile() && pdfFile.getName().endsWith(".pdf")){
                    pdfMetadataXml = client.ExtractDocumentsMetadata(pdfFile.getPath());
                    client.saveToXmlFile(pdfMetadataXml,"D:/360MoveData/Users/asus/Desktop/XML/Metadata/" + "ExtractedData_" + count + ".bib");
                    System.out.println("Metadata " + count + " was extracted successfully.");
                    count++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
