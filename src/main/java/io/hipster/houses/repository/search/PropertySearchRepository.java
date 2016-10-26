package io.hipster.houses.repository.search;

import io.hipster.houses.domain.Property;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Property entity.
 */
public interface PropertySearchRepository extends ElasticsearchRepository<Property, Long> {
}
