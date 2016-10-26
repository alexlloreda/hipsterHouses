package io.hipster.houses.service.impl;

import io.hipster.houses.service.OfferService;
import io.hipster.houses.domain.Offer;
import io.hipster.houses.repository.OfferRepository;
import io.hipster.houses.repository.search.OfferSearchRepository;
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
 * Service Implementation for managing Offer.
 */
@Service
@Transactional
public class OfferServiceImpl implements OfferService{

    private final Logger log = LoggerFactory.getLogger(OfferServiceImpl.class);
    
    @Inject
    private OfferRepository offerRepository;

    @Inject
    private OfferSearchRepository offerSearchRepository;

    /**
     * Save a offer.
     *
     * @param offer the entity to save
     * @return the persisted entity
     */
    public Offer save(Offer offer) {
        log.debug("Request to save Offer : {}", offer);
        Offer result = offerRepository.save(offer);
        offerSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the offers.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Offer> findAll() {
        log.debug("Request to get all Offers");
        List<Offer> result = offerRepository.findAll();

        return result;
    }

    /**
     *  Get one offer by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Offer findOne(Long id) {
        log.debug("Request to get Offer : {}", id);
        Offer offer = offerRepository.findOne(id);
        return offer;
    }

    /**
     *  Delete the  offer by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Offer : {}", id);
        offerRepository.delete(id);
        offerSearchRepository.delete(id);
    }

    /**
     * Search for the offer corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Offer> search(String query) {
        log.debug("Request to search Offers for query {}", query);
        return StreamSupport
            .stream(offerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
