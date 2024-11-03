package cufe.example.BackEndSection;

public class SearchResult {
    private String title;
    private String textAbstract;

    public SearchResult(String title, String textAbstract) {
        this.title = title;
        this.textAbstract = textAbstract;
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
