package au.com.booker.repository.search;
import au.com.booker.domain.BInventory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link BInventory} entity.
 */
public interface BInventorySearchRepository extends ElasticsearchRepository<BInventory, String> {
}
