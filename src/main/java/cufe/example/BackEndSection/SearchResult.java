package cufe.example.BackEndSection;

public class SearchResult {
    private String title;
    private String textAbstract;
    private String highlightText;
    private String uuid;


    public SearchResult(String title, String textAbstract, String highlightText, String uuid) {
        this.title = title;
        this.textAbstract = textAbstract;
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

    public String getTextAbstract() {
        return textAbstract;
    }

    public void setTextAbstract(String textAbstract) {
        this.textAbstract = textAbstract;
    }
}
