package io.hipster.houses.web.rest;

import io.hipster.houses.HipsterHousesApp;

import io.hipster.houses.domain.Listing;
import io.hipster.houses.repository.ListingRepository;
import io.hipster.houses.service.ListingService;
import io.hipster.houses.repository.search.ListingSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ListingResource REST controller.
 *
 * @see ListingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HipsterHousesApp.class)
public class ListingResourceIntTest {

    private static final BigDecimal DEFAULT_MINIMUM_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_MINIMUM_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_BUILT_AREA = new BigDecimal(1);
    private static final BigDecimal UPDATED_BUILT_AREA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_AREA = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AREA = new BigDecimal(2);

    private static final Integer DEFAULT_ROOMS = 1;
    private static final Integer UPDATED_ROOMS = 2;

    private static final Integer DEFAULT_BATHROOMS = 1;
    private static final Integer UPDATED_BATHROOMS = 2;

    private static final Integer DEFAULT_INDOOR_CAR_PARKS = 1;
    private static final Integer UPDATED_INDOOR_CAR_PARKS = 2;

    private static final Integer DEFAULT_OUTDOOR_CAR_PARKS = 1;
    private static final Integer UPDATED_OUTDOOR_CAR_PARKS = 2;

    private static final BigDecimal DEFAULT_SELL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SELL_PRICE = new BigDecimal(2);

    @Inject
    private ListingRepository listingRepository;

    @Inject
    private ListingService listingService;

    @Inject
    private ListingSearchRepository listingSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restListingMockMvc;

    private Listing listing;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ListingResource listingResource = new ListingResource();
        ReflectionTestUtils.setField(listingResource, "listingService", listingService);
        this.restListingMockMvc = MockMvcBuilders.standaloneSetup(listingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Listing createEntity(EntityManager em) {
        Listing listing = new Listing()
                .minimumPrice(DEFAULT_MINIMUM_PRICE)
                .builtArea(DEFAULT_BUILT_AREA)
                .totalArea(DEFAULT_TOTAL_AREA)
                .rooms(DEFAULT_ROOMS)
                .bathrooms(DEFAULT_BATHROOMS)
                .indoorCarParks(DEFAULT_INDOOR_CAR_PARKS)
                .outdoorCarParks(DEFAULT_OUTDOOR_CAR_PARKS)
                .sellPrice(DEFAULT_SELL_PRICE);
        return listing;
    }

    @Before
    public void initTest() {
        listingSearchRepository.deleteAll();
        listing = createEntity(em);
    }

    @Test
    @Transactional
    public void createListing() throws Exception {
        int databaseSizeBeforeCreate = listingRepository.findAll().size();

        // Create the Listing

        restListingMockMvc.perform(post("/api/listings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(listing)))
                .andExpect(status().isCreated());

        // Validate the Listing in the database
        List<Listing> listings = listingRepository.findAll();
        assertThat(listings).hasSize(databaseSizeBeforeCreate + 1);
        Listing testListing = listings.get(listings.size() - 1);
        assertThat(testListing.getMinimumPrice()).isEqualTo(DEFAULT_MINIMUM_PRICE);
        assertThat(testListing.getBuiltArea()).isEqualTo(DEFAULT_BUILT_AREA);
        assertThat(testListing.getTotalArea()).isEqualTo(DEFAULT_TOTAL_AREA);
        assertThat(testListing.getRooms()).isEqualTo(DEFAULT_ROOMS);
        assertThat(testListing.getBathrooms()).isEqualTo(DEFAULT_BATHROOMS);
        assertThat(testListing.getIndoorCarParks()).isEqualTo(DEFAULT_INDOOR_CAR_PARKS);
        assertThat(testListing.getOutdoorCarParks()).isEqualTo(DEFAULT_OUTDOOR_CAR_PARKS);
        assertThat(testListing.getSellPrice()).isEqualTo(DEFAULT_SELL_PRICE);

        // Validate the Listing in ElasticSearch
        Listing listingEs = listingSearchRepository.findOne(testListing.getId());
        assertThat(listingEs).isEqualToComparingFieldByField(testListing);
    }

    @Test
    @Transactional
    public void getAllListings() throws Exception {
        // Initialize the database
        listingRepository.saveAndFlush(listing);

        // Get all the listings
        restListingMockMvc.perform(get("/api/listings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(listing.getId().intValue())))
                .andExpect(jsonPath("$.[*].minimumPrice").value(hasItem(DEFAULT_MINIMUM_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].builtArea").value(hasItem(DEFAULT_BUILT_AREA.intValue())))
                .andExpect(jsonPath("$.[*].totalArea").value(hasItem(DEFAULT_TOTAL_AREA.intValue())))
                .andExpect(jsonPath("$.[*].rooms").value(hasItem(DEFAULT_ROOMS)))
                .andExpect(jsonPath("$.[*].bathrooms").value(hasItem(DEFAULT_BATHROOMS)))
                .andExpect(jsonPath("$.[*].indoorCarParks").value(hasItem(DEFAULT_INDOOR_CAR_PARKS)))
                .andExpect(jsonPath("$.[*].outdoorCarParks").value(hasItem(DEFAULT_OUTDOOR_CAR_PARKS)))
                .andExpect(jsonPath("$.[*].sellPrice").value(hasItem(DEFAULT_SELL_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void getListing() throws Exception {
        // Initialize the database
        listingRepository.saveAndFlush(listing);

        // Get the listing
        restListingMockMvc.perform(get("/api/listings/{id}", listing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(listing.getId().intValue()))
            .andExpect(jsonPath("$.minimumPrice").value(DEFAULT_MINIMUM_PRICE.intValue()))
            .andExpect(jsonPath("$.builtArea").value(DEFAULT_BUILT_AREA.intValue()))
            .andExpect(jsonPath("$.totalArea").value(DEFAULT_TOTAL_AREA.intValue()))
            .andExpect(jsonPath("$.rooms").value(DEFAULT_ROOMS))
            .andExpect(jsonPath("$.bathrooms").value(DEFAULT_BATHROOMS))
            .andExpect(jsonPath("$.indoorCarParks").value(DEFAULT_INDOOR_CAR_PARKS))
            .andExpect(jsonPath("$.outdoorCarParks").value(DEFAULT_OUTDOOR_CAR_PARKS))
            .andExpect(jsonPath("$.sellPrice").value(DEFAULT_SELL_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingListing() throws Exception {
        // Get the listing
        restListingMockMvc.perform(get("/api/listings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateListing() throws Exception {
        // Initialize the database
        listingService.save(listing);

        int databaseSizeBeforeUpdate = listingRepository.findAll().size();

        // Update the listing
        Listing updatedListing = listingRepository.findOne(listing.getId());
        updatedListing
                .minimumPrice(UPDATED_MINIMUM_PRICE)
                .builtArea(UPDATED_BUILT_AREA)
                .totalArea(UPDATED_TOTAL_AREA)
                .rooms(UPDATED_ROOMS)
                .bathrooms(UPDATED_BATHROOMS)
                .indoorCarParks(UPDATED_INDOOR_CAR_PARKS)
                .outdoorCarParks(UPDATED_OUTDOOR_CAR_PARKS)
                .sellPrice(UPDATED_SELL_PRICE);

        restListingMockMvc.perform(put("/api/listings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedListing)))
                .andExpect(status().isOk());

        // Validate the Listing in the database
        List<Listing> listings = listingRepository.findAll();
        assertThat(listings).hasSize(databaseSizeBeforeUpdate);
        Listing testListing = listings.get(listings.size() - 1);
        assertThat(testListing.getMinimumPrice()).isEqualTo(UPDATED_MINIMUM_PRICE);
        assertThat(testListing.getBuiltArea()).isEqualTo(UPDATED_BUILT_AREA);
        assertThat(testListing.getTotalArea()).isEqualTo(UPDATED_TOTAL_AREA);
        assertThat(testListing.getRooms()).isEqualTo(UPDATED_ROOMS);
        assertThat(testListing.getBathrooms()).isEqualTo(UPDATED_BATHROOMS);
        assertThat(testListing.getIndoorCarParks()).isEqualTo(UPDATED_INDOOR_CAR_PARKS);
        assertThat(testListing.getOutdoorCarParks()).isEqualTo(UPDATED_OUTDOOR_CAR_PARKS);
        assertThat(testListing.getSellPrice()).isEqualTo(UPDATED_SELL_PRICE);

        // Validate the Listing in ElasticSearch
        Listing listingEs = listingSearchRepository.findOne(testListing.getId());
        assertThat(listingEs).isEqualToComparingFieldByField(testListing);
    }

    @Test
    @Transactional
    public void deleteListing() throws Exception {
        // Initialize the database
        listingService.save(listing);

        int databaseSizeBeforeDelete = listingRepository.findAll().size();

        // Get the listing
        restListingMockMvc.perform(delete("/api/listings/{id}", listing.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean listingExistsInEs = listingSearchRepository.exists(listing.getId());
        assertThat(listingExistsInEs).isFalse();

        // Validate the database is empty
        List<Listing> listings = listingRepository.findAll();
        assertThat(listings).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchListing() throws Exception {
        // Initialize the database
        listingService.save(listing);

        // Search the listing
        restListingMockMvc.perform(get("/api/_search/listings?query=id:" + listing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(listing.getId().intValue())))
            .andExpect(jsonPath("$.[*].minimumPrice").value(hasItem(DEFAULT_MINIMUM_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].builtArea").value(hasItem(DEFAULT_BUILT_AREA.intValue())))
            .andExpect(jsonPath("$.[*].totalArea").value(hasItem(DEFAULT_TOTAL_AREA.intValue())))
            .andExpect(jsonPath("$.[*].rooms").value(hasItem(DEFAULT_ROOMS)))
            .andExpect(jsonPath("$.[*].bathrooms").value(hasItem(DEFAULT_BATHROOMS)))
            .andExpect(jsonPath("$.[*].indoorCarParks").value(hasItem(DEFAULT_INDOOR_CAR_PARKS)))
            .andExpect(jsonPath("$.[*].outdoorCarParks").value(hasItem(DEFAULT_OUTDOOR_CAR_PARKS)))
            .andExpect(jsonPath("$.[*].sellPrice").value(hasItem(DEFAULT_SELL_PRICE.intValue())));
    }
}
