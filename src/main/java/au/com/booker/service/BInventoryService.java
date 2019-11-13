package au.com.booker.service;

import au.com.booker.domain.BInventory;
import au.com.booker.repository.BInventoryRepository;
import au.com.booker.repository.search.BInventorySearchRepository;
import au.com.booker.service.dto.BInventoryDTO;
import au.com.booker.service.mapper.BInventoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link BInventory}.
 */
@Service
public class BInventoryService {

    private final Logger log = LoggerFactory.getLogger(BInventoryService.class);

    private final BInventoryRepository bInventoryRepository;

    private final BInventoryMapper bInventoryMapper;

    private final BInventorySearchRepository bInventorySearchRepository;

    public BInventoryService(BInventoryRepository bInventoryRepository, BInventoryMapper bInventoryMapper, BInventorySearchRepository bInventorySearchRepository) {
        this.bInventoryRepository = bInventoryRepository;
        this.bInventoryMapper = bInventoryMapper;
        this.bInventorySearchRepository = bInventorySearchRepository;
    }

    /**
     * Save a bInventory.
     *
     * @param bInventoryDTO the entity to save.
     * @return the persisted entity.
     */
    public BInventoryDTO save(BInventoryDTO bInventoryDTO) {
        log.debug("Request to save BInventory : {}", bInventoryDTO);
        BInventory bInventory = bInventoryMapper.toEntity(bInventoryDTO);
        bInventory = bInventoryRepository.save(bInventory);
        BInventoryDTO result = bInventoryMapper.toDto(bInventory);
        bInventorySearchRepository.save(bInventory);
        return result;
    }

    /**
     * Get all the bInventories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<BInventoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BInventories");
        return bInventoryRepository.findAll(pageable)
            .map(bInventoryMapper::toDto);
    }


    /**
     * Get one bInventory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<BInventoryDTO> findOne(String id) {
        log.debug("Request to get BInventory : {}", id);
        return bInventoryRepository.findById(id)
            .map(bInventoryMapper::toDto);
    }

    /**
     * Delete the bInventory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete BInventory : {}", id);
        bInventoryRepository.deleteById(id);
        bInventorySearchRepository.deleteById(id);
    }

    /**
     * Search for the bInventory corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<BInventoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BInventories for query {}", query);
        return bInventorySearchRepository.search(queryStringQuery(query), pageable)
            .map(bInventoryMapper::toDto);
    }
}
