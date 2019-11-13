package au.com.booker.web.rest;

import au.com.booker.BookerInventoryApp;
import au.com.booker.config.TestSecurityConfiguration;
import au.com.booker.domain.BInventory;
import au.com.booker.repository.BInventoryRepository;
import au.com.booker.repository.search.BInventorySearchRepository;
import au.com.booker.service.BInventoryService;
import au.com.booker.service.dto.BInventoryDTO;
import au.com.booker.service.mapper.BInventoryMapper;
import au.com.booker.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static au.com.booker.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link BInventoryResource} REST controller.
 */
@SpringBootTest(classes = {BookerInventoryApp.class, TestSecurityConfiguration.class})
public class BInventoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final Double DEFAULT_PURCHASE_PRICE = 1D;
    private static final Double UPDATED_PURCHASE_PRICE = 2D;

    private static final Double DEFAULT_SELLING_PRICE = 0.0D;
    private static final Double UPDATED_SELLING_PRICE = 1D;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Double DEFAULT_DISCOUNTS = 1D;
    private static final Double UPDATED_DISCOUNTS = 2D;

    private static final Integer DEFAULT_UNITS_IN_STORE = 1;
    private static final Integer UPDATED_UNITS_IN_STORE = 2;

    private static final LocalDate DEFAULT_LAST_DATE_OF_PURCHASE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_DATE_OF_PURCHASE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private BInventoryRepository bInventoryRepository;

    @Autowired
    private BInventoryMapper bInventoryMapper;

    @Autowired
    private BInventoryService bInventoryService;

    /**
     * This repository is mocked in the au.com.booker.repository.search test package.
     *
     * @see au.com.booker.repository.search.BInventorySearchRepositoryMockConfiguration
     */
    @Autowired
    private BInventorySearchRepository mockBInventorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restBInventoryMockMvc;

    private BInventory bInventory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BInventoryResource bInventoryResource = new BInventoryResource(bInventoryService);
        this.restBInventoryMockMvc = MockMvcBuilders.standaloneSetup(bInventoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BInventory createEntity() {
        BInventory bInventory = new BInventory()
            .name(DEFAULT_NAME)
            .category(DEFAULT_CATEGORY)
            .purchasePrice(DEFAULT_PURCHASE_PRICE)
            .sellingPrice(DEFAULT_SELLING_PRICE)
            .notes(DEFAULT_NOTES)
            .discounts(DEFAULT_DISCOUNTS)
            .unitsInStore(DEFAULT_UNITS_IN_STORE)
            .lastDateOfPurchase(DEFAULT_LAST_DATE_OF_PURCHASE);
        return bInventory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BInventory createUpdatedEntity() {
        BInventory bInventory = new BInventory()
            .name(UPDATED_NAME)
            .category(UPDATED_CATEGORY)
            .purchasePrice(UPDATED_PURCHASE_PRICE)
            .sellingPrice(UPDATED_SELLING_PRICE)
            .notes(UPDATED_NOTES)
            .discounts(UPDATED_DISCOUNTS)
            .unitsInStore(UPDATED_UNITS_IN_STORE)
            .lastDateOfPurchase(UPDATED_LAST_DATE_OF_PURCHASE);
        return bInventory;
    }

    @BeforeEach
    public void initTest() {
        bInventoryRepository.deleteAll();
        bInventory = createEntity();
    }

    @Test
    public void createBInventory() throws Exception {
        int databaseSizeBeforeCreate = bInventoryRepository.findAll().size();

        // Create the BInventory
        BInventoryDTO bInventoryDTO = bInventoryMapper.toDto(bInventory);
        restBInventoryMockMvc.perform(post("/api/b-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bInventoryDTO)))
            .andExpect(status().isCreated());

        // Validate the BInventory in the database
        List<BInventory> bInventoryList = bInventoryRepository.findAll();
        assertThat(bInventoryList).hasSize(databaseSizeBeforeCreate + 1);
        BInventory testBInventory = bInventoryList.get(bInventoryList.size() - 1);
        assertThat(testBInventory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBInventory.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testBInventory.getPurchasePrice()).isEqualTo(DEFAULT_PURCHASE_PRICE);
        assertThat(testBInventory.getSellingPrice()).isEqualTo(DEFAULT_SELLING_PRICE);
        assertThat(testBInventory.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testBInventory.getDiscounts()).isEqualTo(DEFAULT_DISCOUNTS);
        assertThat(testBInventory.getUnitsInStore()).isEqualTo(DEFAULT_UNITS_IN_STORE);
        assertThat(testBInventory.getLastDateOfPurchase()).isEqualTo(DEFAULT_LAST_DATE_OF_PURCHASE);

        // Validate the BInventory in Elasticsearch
        verify(mockBInventorySearchRepository, times(1)).save(testBInventory);
    }

    @Test
    public void createBInventoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bInventoryRepository.findAll().size();

        // Create the BInventory with an existing ID
        bInventory.setId("existing_id");
        BInventoryDTO bInventoryDTO = bInventoryMapper.toDto(bInventory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBInventoryMockMvc.perform(post("/api/b-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bInventoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BInventory in the database
        List<BInventory> bInventoryList = bInventoryRepository.findAll();
        assertThat(bInventoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the BInventory in Elasticsearch
        verify(mockBInventorySearchRepository, times(0)).save(bInventory);
    }


    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bInventoryRepository.findAll().size();
        // set the field null
        bInventory.setName(null);

        // Create the BInventory, which fails.
        BInventoryDTO bInventoryDTO = bInventoryMapper.toDto(bInventory);

        restBInventoryMockMvc.perform(post("/api/b-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bInventoryDTO)))
            .andExpect(status().isBadRequest());

        List<BInventory> bInventoryList = bInventoryRepository.findAll();
        assertThat(bInventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = bInventoryRepository.findAll().size();
        // set the field null
        bInventory.setCategory(null);

        // Create the BInventory, which fails.
        BInventoryDTO bInventoryDTO = bInventoryMapper.toDto(bInventory);

        restBInventoryMockMvc.perform(post("/api/b-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bInventoryDTO)))
            .andExpect(status().isBadRequest());

        List<BInventory> bInventoryList = bInventoryRepository.findAll();
        assertThat(bInventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkPurchasePriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = bInventoryRepository.findAll().size();
        // set the field null
        bInventory.setPurchasePrice(null);

        // Create the BInventory, which fails.
        BInventoryDTO bInventoryDTO = bInventoryMapper.toDto(bInventory);

        restBInventoryMockMvc.perform(post("/api/b-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bInventoryDTO)))
            .andExpect(status().isBadRequest());

        List<BInventory> bInventoryList = bInventoryRepository.findAll();
        assertThat(bInventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkSellingPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = bInventoryRepository.findAll().size();
        // set the field null
        bInventory.setSellingPrice(null);

        // Create the BInventory, which fails.
        BInventoryDTO bInventoryDTO = bInventoryMapper.toDto(bInventory);

        restBInventoryMockMvc.perform(post("/api/b-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bInventoryDTO)))
            .andExpect(status().isBadRequest());

        List<BInventory> bInventoryList = bInventoryRepository.findAll();
        assertThat(bInventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkUnitsInStoreIsRequired() throws Exception {
        int databaseSizeBeforeTest = bInventoryRepository.findAll().size();
        // set the field null
        bInventory.setUnitsInStore(null);

        // Create the BInventory, which fails.
        BInventoryDTO bInventoryDTO = bInventoryMapper.toDto(bInventory);

        restBInventoryMockMvc.perform(post("/api/b-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bInventoryDTO)))
            .andExpect(status().isBadRequest());

        List<BInventory> bInventoryList = bInventoryRepository.findAll();
        assertThat(bInventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllBInventories() throws Exception {
        // Initialize the database
        bInventoryRepository.save(bInventory);

        // Get all the bInventoryList
        restBInventoryMockMvc.perform(get("/api/b-inventories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bInventory.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].purchasePrice").value(hasItem(DEFAULT_PURCHASE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].sellingPrice").value(hasItem(DEFAULT_SELLING_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].discounts").value(hasItem(DEFAULT_DISCOUNTS.doubleValue())))
            .andExpect(jsonPath("$.[*].unitsInStore").value(hasItem(DEFAULT_UNITS_IN_STORE)))
            .andExpect(jsonPath("$.[*].lastDateOfPurchase").value(hasItem(DEFAULT_LAST_DATE_OF_PURCHASE.toString())));
    }
    
    @Test
    public void getBInventory() throws Exception {
        // Initialize the database
        bInventoryRepository.save(bInventory);

        // Get the bInventory
        restBInventoryMockMvc.perform(get("/api/b-inventories/{id}", bInventory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bInventory.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.purchasePrice").value(DEFAULT_PURCHASE_PRICE.doubleValue()))
            .andExpect(jsonPath("$.sellingPrice").value(DEFAULT_SELLING_PRICE.doubleValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.discounts").value(DEFAULT_DISCOUNTS.doubleValue()))
            .andExpect(jsonPath("$.unitsInStore").value(DEFAULT_UNITS_IN_STORE))
            .andExpect(jsonPath("$.lastDateOfPurchase").value(DEFAULT_LAST_DATE_OF_PURCHASE.toString()));
    }

    @Test
    public void getNonExistingBInventory() throws Exception {
        // Get the bInventory
        restBInventoryMockMvc.perform(get("/api/b-inventories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateBInventory() throws Exception {
        // Initialize the database
        bInventoryRepository.save(bInventory);

        int databaseSizeBeforeUpdate = bInventoryRepository.findAll().size();

        // Update the bInventory
        BInventory updatedBInventory = bInventoryRepository.findById(bInventory.getId()).get();
        updatedBInventory
            .name(UPDATED_NAME)
            .category(UPDATED_CATEGORY)
            .purchasePrice(UPDATED_PURCHASE_PRICE)
            .sellingPrice(UPDATED_SELLING_PRICE)
            .notes(UPDATED_NOTES)
            .discounts(UPDATED_DISCOUNTS)
            .unitsInStore(UPDATED_UNITS_IN_STORE)
            .lastDateOfPurchase(UPDATED_LAST_DATE_OF_PURCHASE);
        BInventoryDTO bInventoryDTO = bInventoryMapper.toDto(updatedBInventory);

        restBInventoryMockMvc.perform(put("/api/b-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bInventoryDTO)))
            .andExpect(status().isOk());

        // Validate the BInventory in the database
        List<BInventory> bInventoryList = bInventoryRepository.findAll();
        assertThat(bInventoryList).hasSize(databaseSizeBeforeUpdate);
        BInventory testBInventory = bInventoryList.get(bInventoryList.size() - 1);
        assertThat(testBInventory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBInventory.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testBInventory.getPurchasePrice()).isEqualTo(UPDATED_PURCHASE_PRICE);
        assertThat(testBInventory.getSellingPrice()).isEqualTo(UPDATED_SELLING_PRICE);
        assertThat(testBInventory.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testBInventory.getDiscounts()).isEqualTo(UPDATED_DISCOUNTS);
        assertThat(testBInventory.getUnitsInStore()).isEqualTo(UPDATED_UNITS_IN_STORE);
        assertThat(testBInventory.getLastDateOfPurchase()).isEqualTo(UPDATED_LAST_DATE_OF_PURCHASE);

        // Validate the BInventory in Elasticsearch
        verify(mockBInventorySearchRepository, times(1)).save(testBInventory);
    }

    @Test
    public void updateNonExistingBInventory() throws Exception {
        int databaseSizeBeforeUpdate = bInventoryRepository.findAll().size();

        // Create the BInventory
        BInventoryDTO bInventoryDTO = bInventoryMapper.toDto(bInventory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBInventoryMockMvc.perform(put("/api/b-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bInventoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BInventory in the database
        List<BInventory> bInventoryList = bInventoryRepository.findAll();
        assertThat(bInventoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BInventory in Elasticsearch
        verify(mockBInventorySearchRepository, times(0)).save(bInventory);
    }

    @Test
    public void deleteBInventory() throws Exception {
        // Initialize the database
        bInventoryRepository.save(bInventory);

        int databaseSizeBeforeDelete = bInventoryRepository.findAll().size();

        // Delete the bInventory
        restBInventoryMockMvc.perform(delete("/api/b-inventories/{id}", bInventory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BInventory> bInventoryList = bInventoryRepository.findAll();
        assertThat(bInventoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the BInventory in Elasticsearch
        verify(mockBInventorySearchRepository, times(1)).deleteById(bInventory.getId());
    }

    @Test
    public void searchBInventory() throws Exception {
        // Initialize the database
        bInventoryRepository.save(bInventory);
        when(mockBInventorySearchRepository.search(queryStringQuery("id:" + bInventory.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(bInventory), PageRequest.of(0, 1), 1));
        // Search the bInventory
        restBInventoryMockMvc.perform(get("/api/_search/b-inventories?query=id:" + bInventory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bInventory.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].purchasePrice").value(hasItem(DEFAULT_PURCHASE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].sellingPrice").value(hasItem(DEFAULT_SELLING_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].discounts").value(hasItem(DEFAULT_DISCOUNTS.doubleValue())))
            .andExpect(jsonPath("$.[*].unitsInStore").value(hasItem(DEFAULT_UNITS_IN_STORE)))
            .andExpect(jsonPath("$.[*].lastDateOfPurchase").value(hasItem(DEFAULT_LAST_DATE_OF_PURCHASE.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BInventory.class);
        BInventory bInventory1 = new BInventory();
        bInventory1.setId("id1");
        BInventory bInventory2 = new BInventory();
        bInventory2.setId(bInventory1.getId());
        assertThat(bInventory1).isEqualTo(bInventory2);
        bInventory2.setId("id2");
        assertThat(bInventory1).isNotEqualTo(bInventory2);
        bInventory1.setId(null);
        assertThat(bInventory1).isNotEqualTo(bInventory2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BInventoryDTO.class);
        BInventoryDTO bInventoryDTO1 = new BInventoryDTO();
        bInventoryDTO1.setId("id1");
        BInventoryDTO bInventoryDTO2 = new BInventoryDTO();
        assertThat(bInventoryDTO1).isNotEqualTo(bInventoryDTO2);
        bInventoryDTO2.setId(bInventoryDTO1.getId());
        assertThat(bInventoryDTO1).isEqualTo(bInventoryDTO2);
        bInventoryDTO2.setId("id2");
        assertThat(bInventoryDTO1).isNotEqualTo(bInventoryDTO2);
        bInventoryDTO1.setId(null);
        assertThat(bInventoryDTO1).isNotEqualTo(bInventoryDTO2);
    }
}
