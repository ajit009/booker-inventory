package au.com.booker.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link BInventorySearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class BInventorySearchRepositoryMockConfiguration {

    @MockBean
    private BInventorySearchRepository mockBInventorySearchRepository;

}
