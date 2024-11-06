package cufe.example.BackEndSection.ExternalFunction;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface FullTextRepository extends MongoRepository<FullTextEntity, String> {
    FullTextEntity findByTitle(String title);
}
