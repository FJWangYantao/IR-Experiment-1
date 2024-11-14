package cufe.example.BackEndSection.ExternalFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;


    public Article findArticleByUuid(String uuid){
        System.out.println("Searching for article: " + uuid);
        return articleRepository.findByUuid(uuid);
    }

    public boolean isArticleFavored(String uuid) {
        Article article = articleRepository.findByUuid(uuid);
        return article != null && article.isFavored();
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public ResponseEntity<Article> createArticle(Article article) {
        articleRepository.save(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(article);
    }

    public ResponseEntity<Void> deleteArticle(String uuid) {
        articleRepository.deleteByUuid(uuid);
        return ResponseEntity.noContent().build();
    }

}
