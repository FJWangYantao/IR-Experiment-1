package cufe.example.BackEndSection;

public class SearchResult {
    private String title;
    private String author;
    private String highlightText;
    private String uuid;
//    private int numberOfResult;


    public SearchResult(String title, String author, String highlightText, String uuid) {
        this.title = title;
        this.author = author;
        this.highlightText = highlightText;
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getHighlightText() {
        return highlightText;
    }

    public void setHighlightText(String highlightText) {
        this.highlightText = highlightText;
    }



    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
