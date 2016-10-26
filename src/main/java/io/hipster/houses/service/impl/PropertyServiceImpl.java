package io.hipster.houses.service.impl;

import io.hipster.houses.service.PropertyService;
import io.hipster.houses.domain.Property;
import io.hipster.houses.repository.PropertyRepository;
import io.hipster.houses.repository.search.PropertySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Property.
 */
@Service
@Transactional
public class PropertyServiceImpl implements PropertyService{

    private final Logger log = LoggerFactory.getLogger(PropertyServiceImpl.class);
    
    @Inject
    private PropertyRepository propertyRepository;

    @Inject
    private PropertySearchRepository propertySearchRepository;

    /**
     * Save a property.
     *
     * @param property the entity to save
     * @return the persisted entity
     */
    public Property save(Property property) {
        log.debug("Request to save Property : {}", property);
        Property result = propertyRepository.save(property);
        propertySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the properties.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Property> findAll() {
        log.debug("Request to get all Properties");
        List<Property> result = propertyRepository.findAll();

        return result;
    }

    /**
     *  Get one property by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Property findOne(Long id) {
        log.debug("Request to get Property : {}", id);
        Property property = propertyRepository.findOne(id);
        return property;
    }

    /**
     *  Delete the  property by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Property : {}", id);
        propertyRepository.delete(id);
        propertySearchRepository.delete(id);
    }

    /**
     * Search for the property corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Property> search(String query) {
        log.debug("Request to search Properties for query {}", query);
        return StreamSupport
            .stream(propertySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
