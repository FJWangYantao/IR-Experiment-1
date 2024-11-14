package cufe.example.BackEndSection.ExternalFunction;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "articles")

public class Article {

        @Id
        private String id;
        private String title;
        private String uuid;
        private String author;
        private boolean isFavored;

        public Article(String title, String uuid, String author,boolean isFavored){
            this.title = title;
            this.uuid = uuid;
            this.author = author;
            this.isFavored = isFavored;
        }

    public boolean isFavored() {
        return isFavored;
    }

    public void setFavored(boolean favored) {
        isFavored = favored;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
