package io.hipster.houses.repository.search;

import io.hipster.houses.domain.Listing;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Listing entity.
 */
public interface ListingSearchRepository extends ElasticsearchRepository<Listing, Long> {
}
