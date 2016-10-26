package io.hipster.houses.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.hipster.houses.domain.Listing;
import io.hipster.houses.service.ListingService;
import io.hipster.houses.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Listing.
 */
@RestController
@RequestMapping("/api")
public class ListingResource {

    private final Logger log = LoggerFactory.getLogger(ListingResource.class);
        
    @Inject
    private ListingService listingService;

    /**
     * POST  /listings : Create a new listing.
     *
     * @param listing the listing to create
     * @return the ResponseEntity with status 201 (Created) and with body the new listing, or with status 400 (Bad Request) if the listing has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/listings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Listing> createListing(@RequestBody Listing listing) throws URISyntaxException {
        log.debug("REST request to save Listing : {}", listing);
        if (listing.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("listing", "idexists", "A new listing cannot already have an ID")).body(null);
        }
        Listing result = listingService.save(listing);
        return ResponseEntity.created(new URI("/api/listings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("listing", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /listings : Updates an existing listing.
     *
     * @param listing the listing to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated listing,
     * or with status 400 (Bad Request) if the listing is not valid,
     * or with status 500 (Internal Server Error) if the listing couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/listings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Listing> updateListing(@RequestBody Listing listing) throws URISyntaxException {
        log.debug("REST request to update Listing : {}", listing);
        if (listing.getId() == null) {
            return createListing(listing);
        }
        Listing result = listingService.save(listing);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("listing", listing.getId().toString()))
            .body(result);
    }

    /**
     * GET  /listings : get all the listings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of listings in body
     */
    @RequestMapping(value = "/listings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Listing> getAllListings() {
        log.debug("REST request to get all Listings");
        return listingService.findAll();
    }

    /**
     * GET  /listings/:id : get the "id" listing.
     *
     * @param id the id of the listing to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the listing, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/listings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Listing> getListing(@PathVariable Long id) {
        log.debug("REST request to get Listing : {}", id);
        Listing listing = listingService.findOne(id);
        return Optional.ofNullable(listing)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /listings/:id : delete the "id" listing.
     *
     * @param id the id of the listing to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/listings/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteListing(@PathVariable Long id) {
        log.debug("REST request to delete Listing : {}", id);
        listingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("listing", id.toString())).build();
    }

    /**
     * SEARCH  /_search/listings?query=:query : search for the listing corresponding
     * to the query.
     *
     * @param query the query of the listing search 
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/listings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Listing> searchListings(@RequestParam String query) {
        log.debug("REST request to search Listings for query {}", query);
        return listingService.search(query);
    }


}
