package cufe.example.BackEndSection.ExternalFunction;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticleRepository extends MongoRepository<Article, String> {
    Article findByUuid(String uuid);
    boolean existsByUuidAndIsFavored(String uuid, boolean isFavored);
    void deleteByUuid(String uuid);
}
