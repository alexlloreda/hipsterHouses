package io.hipster.houses.service;

import io.hipster.houses.domain.Offer;

import java.util.List;

/**
 * Service Interface for managing Offer.
 */
public interface OfferService {

    /**
     * Save a offer.
     *
     * @param offer the entity to save
     * @return the persisted entity
     */
    Offer save(Offer offer);

    /**
     *  Get all the offers.
     *  
     *  @return the list of entities
     */
    List<Offer> findAll();

    /**
     *  Get the "id" offer.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Offer findOne(Long id);

    /**
     *  Delete the "id" offer.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the offer corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<Offer> search(String query);
}
