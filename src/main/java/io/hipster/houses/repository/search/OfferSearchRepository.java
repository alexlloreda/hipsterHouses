package io.hipster.houses.repository.search;

import io.hipster.houses.domain.Offer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Offer entity.
 */
public interface OfferSearchRepository extends ElasticsearchRepository<Offer, Long> {
}
