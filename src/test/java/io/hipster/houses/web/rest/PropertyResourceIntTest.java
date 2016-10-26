package io.hipster.houses.web.rest;

import io.hipster.houses.HipsterHousesApp;

import io.hipster.houses.domain.Property;
import io.hipster.houses.repository.PropertyRepository;
import io.hipster.houses.service.PropertyService;
import io.hipster.houses.repository.search.PropertySearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PropertyResource REST controller.
 *
 * @see PropertyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HipsterHousesApp.class)
public class PropertyResourceIntTest {

    private static final String DEFAULT_STREET_ADDRESS = "AAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBB";

    private static final String DEFAULT_CITY = "AAAAA";
    private static final String UPDATED_CITY = "BBBBB";

    private static final String DEFAULT_STATE_PROVINCE = "AAAAA";
    private static final String UPDATED_STATE_PROVINCE = "BBBBB";

    @Inject
    private PropertyRepository propertyRepository;

    @Inject
    private PropertyService propertyService;

    @Inject
    private PropertySearchRepository propertySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPropertyMockMvc;

    private Property property;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PropertyResource propertyResource = new PropertyResource();
        ReflectionTestUtils.setField(propertyResource, "propertyService", propertyService);
        this.restPropertyMockMvc = MockMvcBuilders.standaloneSetup(propertyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Property createEntity(EntityManager em) {
        Property property = new Property()
                .streetAddress(DEFAULT_STREET_ADDRESS)
                .postalCode(DEFAULT_POSTAL_CODE)
                .city(DEFAULT_CITY)
                .stateProvince(DEFAULT_STATE_PROVINCE);
        return property;
    }

    @Before
    public void initTest() {
        propertySearchRepository.deleteAll();
        property = createEntity(em);
    }

    @Test
    @Transactional
    public void createProperty() throws Exception {
        int databaseSizeBeforeCreate = propertyRepository.findAll().size();

        // Create the Property

        restPropertyMockMvc.perform(post("/api/properties")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(property)))
                .andExpect(status().isCreated());

        // Validate the Property in the database
        List<Property> properties = propertyRepository.findAll();
        assertThat(properties).hasSize(databaseSizeBeforeCreate + 1);
        Property testProperty = properties.get(properties.size() - 1);
        assertThat(testProperty.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testProperty.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testProperty.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testProperty.getStateProvince()).isEqualTo(DEFAULT_STATE_PROVINCE);

        // Validate the Property in ElasticSearch
        Property propertyEs = propertySearchRepository.findOne(testProperty.getId());
        assertThat(propertyEs).isEqualToComparingFieldByField(testProperty);
    }

    @Test
    @Transactional
    public void getAllProperties() throws Exception {
        // Initialize the database
        propertyRepository.saveAndFlush(property);

        // Get all the properties
        restPropertyMockMvc.perform(get("/api/properties?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(property.getId().intValue())))
                .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE.toString())));
    }

    @Test
    @Transactional
    public void getProperty() throws Exception {
        // Initialize the database
        propertyRepository.saveAndFlush(property);

        // Get the property
        restPropertyMockMvc.perform(get("/api/properties/{id}", property.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(property.getId().intValue()))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS.toString()))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.stateProvince").value(DEFAULT_STATE_PROVINCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProperty() throws Exception {
        // Get the property
        restPropertyMockMvc.perform(get("/api/properties/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProperty() throws Exception {
        // Initialize the database
        propertyService.save(property);

        int databaseSizeBeforeUpdate = propertyRepository.findAll().size();

        // Update the property
        Property updatedProperty = propertyRepository.findOne(property.getId());
        updatedProperty
                .streetAddress(UPDATED_STREET_ADDRESS)
                .postalCode(UPDATED_POSTAL_CODE)
                .city(UPDATED_CITY)
                .stateProvince(UPDATED_STATE_PROVINCE);

        restPropertyMockMvc.perform(put("/api/properties")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProperty)))
                .andExpect(status().isOk());

        // Validate the Property in the database
        List<Property> properties = propertyRepository.findAll();
        assertThat(properties).hasSize(databaseSizeBeforeUpdate);
        Property testProperty = properties.get(properties.size() - 1);
        assertThat(testProperty.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testProperty.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testProperty.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testProperty.getStateProvince()).isEqualTo(UPDATED_STATE_PROVINCE);

        // Validate the Property in ElasticSearch
        Property propertyEs = propertySearchRepository.findOne(testProperty.getId());
        assertThat(propertyEs).isEqualToComparingFieldByField(testProperty);
    }

    @Test
    @Transactional
    public void deleteProperty() throws Exception {
        // Initialize the database
        propertyService.save(property);

        int databaseSizeBeforeDelete = propertyRepository.findAll().size();

        // Get the property
        restPropertyMockMvc.perform(delete("/api/properties/{id}", property.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean propertyExistsInEs = propertySearchRepository.exists(property.getId());
        assertThat(propertyExistsInEs).isFalse();

        // Validate the database is empty
        List<Property> properties = propertyRepository.findAll();
        assertThat(properties).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProperty() throws Exception {
        // Initialize the database
        propertyService.save(property);

        // Search the property
        restPropertyMockMvc.perform(get("/api/_search/properties?query=id:" + property.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(property.getId().intValue())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE.toString())));
    }
}
