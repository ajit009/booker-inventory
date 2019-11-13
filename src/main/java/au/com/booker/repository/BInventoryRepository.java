package au.com.booker.repository;
import au.com.booker.domain.BInventory;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the BInventory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BInventoryRepository extends MongoRepository<BInventory, String> {

}
