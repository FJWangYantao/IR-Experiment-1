package cufe.example.BackEndSection.ExternalFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/getArticles")
    public List<Article> getAllArticles(){
        List<Article> articles = articleService.getAllArticles();
        System.out.println("The article has been gotten.");
        return articles;
    }

    @GetMapping("/checkFavorite/{uuid}")
    public ResponseEntity<Boolean> isArticleFavored(@PathVariable String uuid) {
        boolean isFavored = articleService.isArticleFavored(uuid);
        return ResponseEntity.ok(isFavored); // 返回收藏状态
    }

    @DeleteMapping("deleteArticle/{uuid}")
    public void deleteArticle(@PathVariable String uuid){
        System.out.println("Article " + uuid + " has been deleted.");
        articleService.deleteArticle(uuid);
    }

    @PostMapping("/createArticle")
    public ResponseEntity<Article> createArticle(@RequestParam String uuid,@RequestParam String title,@RequestParam String author) {
        Article article = new Article(title, uuid, author, true);
        System.out.println("Article" + article.getUuid() + " has been created.");
        return articleService.createArticle(article);
    }

}
