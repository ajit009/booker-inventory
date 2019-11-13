package au.com.booker.web.rest;

import au.com.booker.service.BInventoryService;
import au.com.booker.web.rest.errors.BadRequestAlertException;
import au.com.booker.service.dto.BInventoryDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link au.com.booker.domain.BInventory}.
 */
@RestController
@RequestMapping("/api")
public class BInventoryResource {

    private final Logger log = LoggerFactory.getLogger(BInventoryResource.class);

    private static final String ENTITY_NAME = "bookerInventoryBInventory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BInventoryService bInventoryService;

    public BInventoryResource(BInventoryService bInventoryService) {
        this.bInventoryService = bInventoryService;
    }

    /**
     * {@code POST  /b-inventories} : Create a new bInventory.
     *
     * @param bInventoryDTO the bInventoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bInventoryDTO, or with status {@code 400 (Bad Request)} if the bInventory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/b-inventories")
    public ResponseEntity<BInventoryDTO> createBInventory(@Valid @RequestBody BInventoryDTO bInventoryDTO) throws URISyntaxException {
        log.debug("REST request to save BInventory : {}", bInventoryDTO);
        if (bInventoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new bInventory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BInventoryDTO result = bInventoryService.save(bInventoryDTO);
        return ResponseEntity.created(new URI("/api/b-inventories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /b-inventories} : Updates an existing bInventory.
     *
     * @param bInventoryDTO the bInventoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bInventoryDTO,
     * or with status {@code 400 (Bad Request)} if the bInventoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bInventoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/b-inventories")
    public ResponseEntity<BInventoryDTO> updateBInventory(@Valid @RequestBody BInventoryDTO bInventoryDTO) throws URISyntaxException {
        log.debug("REST request to update BInventory : {}", bInventoryDTO);
        if (bInventoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BInventoryDTO result = bInventoryService.save(bInventoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bInventoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /b-inventories} : get all the bInventories.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bInventories in body.
     */
    @GetMapping("/b-inventories")
    public ResponseEntity<List<BInventoryDTO>> getAllBInventories(Pageable pageable) {
        log.debug("REST request to get a page of BInventories");
        Page<BInventoryDTO> page = bInventoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /b-inventories/:id} : get the "id" bInventory.
     *
     * @param id the id of the bInventoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bInventoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/b-inventories/{id}")
    public ResponseEntity<BInventoryDTO> getBInventory(@PathVariable String id) {
        log.debug("REST request to get BInventory : {}", id);
        Optional<BInventoryDTO> bInventoryDTO = bInventoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bInventoryDTO);
    }

    /**
     * {@code DELETE  /b-inventories/:id} : delete the "id" bInventory.
     *
     * @param id the id of the bInventoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/b-inventories/{id}")
    public ResponseEntity<Void> deleteBInventory(@PathVariable String id) {
        log.debug("REST request to delete BInventory : {}", id);
        bInventoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/b-inventories?query=:query} : search for the bInventory corresponding
     * to the query.
     *
     * @param query the query of the bInventory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/b-inventories")
    public ResponseEntity<List<BInventoryDTO>> searchBInventories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of BInventories for query {}", query);
        Page<BInventoryDTO> page = bInventoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
