package io.hipster.houses.service;

import io.hipster.houses.domain.Listing;

import java.util.List;

/**
 * Service Interface for managing Listing.
 */
public interface ListingService {

    /**
     * Save a listing.
     *
     * @param listing the entity to save
     * @return the persisted entity
     */
    Listing save(Listing listing);

    /**
     *  Get all the listings.
     *  
     *  @return the list of entities
     */
    List<Listing> findAll();

    /**
     *  Get the "id" listing.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Listing findOne(Long id);

    /**
     *  Delete the "id" listing.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the listing corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<Listing> search(String query);
}
