package io.hipster.houses.service.impl;

import io.hipster.houses.service.ListingService;
import io.hipster.houses.domain.Listing;
import io.hipster.houses.repository.ListingRepository;
import io.hipster.houses.repository.search.ListingSearchRepository;
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
 * Service Implementation for managing Listing.
 */
@Service
@Transactional
public class ListingServiceImpl implements ListingService{

    private final Logger log = LoggerFactory.getLogger(ListingServiceImpl.class);
    
    @Inject
    private ListingRepository listingRepository;

    @Inject
    private ListingSearchRepository listingSearchRepository;

    /**
     * Save a listing.
     *
     * @param listing the entity to save
     * @return the persisted entity
     */
    public Listing save(Listing listing) {
        log.debug("Request to save Listing : {}", listing);
        Listing result = listingRepository.save(listing);
        listingSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the listings.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Listing> findAll() {
        log.debug("Request to get all Listings");
        List<Listing> result = listingRepository.findAll();

        return result;
    }

    /**
     *  Get one listing by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Listing findOne(Long id) {
        log.debug("Request to get Listing : {}", id);
        Listing listing = listingRepository.findOne(id);
        return listing;
    }

    /**
     *  Delete the  listing by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Listing : {}", id);
        listingRepository.delete(id);
        listingSearchRepository.delete(id);
    }

    /**
     * Search for the listing corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Listing> search(String query) {
        log.debug("Request to search Listings for query {}", query);
        return StreamSupport
            .stream(listingSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
