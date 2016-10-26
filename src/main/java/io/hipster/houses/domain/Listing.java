package io.hipster.houses.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Listing.
 */
@Entity
@Table(name = "listing")
@Document(indexName = "listing")
public class Listing implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "minimum_price", precision=10, scale=2)
    private BigDecimal minimumPrice;

    @Column(name = "built_area", precision=10, scale=2)
    private BigDecimal builtArea;

    @Column(name = "total_area", precision=10, scale=2)
    private BigDecimal totalArea;

    @Column(name = "rooms")
    private Integer rooms;

    @Column(name = "bathrooms")
    private Integer bathrooms;

    @Column(name = "indoor_car_parks")
    private Integer indoorCarParks;

    @Column(name = "outdoor_car_parks")
    private Integer outdoorCarParks;

    @Column(name = "sell_price", precision=10, scale=2)
    private BigDecimal sellPrice;

    @OneToOne
    @JoinColumn(unique = true)
    private Property property;

    @OneToMany(mappedBy = "for")
    @JsonIgnore
    private Set<Offer> offers = new HashSet<>();

    @ManyToOne
    private Person person;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMinimumPrice() {
        return minimumPrice;
    }

    public Listing minimumPrice(BigDecimal minimumPrice) {
        this.minimumPrice = minimumPrice;
        return this;
    }

    public void setMinimumPrice(BigDecimal minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public BigDecimal getBuiltArea() {
        return builtArea;
    }

    public Listing builtArea(BigDecimal builtArea) {
        this.builtArea = builtArea;
        return this;
    }

    public void setBuiltArea(BigDecimal builtArea) {
        this.builtArea = builtArea;
    }

    public BigDecimal getTotalArea() {
        return totalArea;
    }

    public Listing totalArea(BigDecimal totalArea) {
        this.totalArea = totalArea;
        return this;
    }

    public void setTotalArea(BigDecimal totalArea) {
        this.totalArea = totalArea;
    }

    public Integer getRooms() {
        return rooms;
    }

    public Listing rooms(Integer rooms) {
        this.rooms = rooms;
        return this;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Integer getBathrooms() {
        return bathrooms;
    }

    public Listing bathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
        return this;
    }

    public void setBathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
    }

    public Integer getIndoorCarParks() {
        return indoorCarParks;
    }

    public Listing indoorCarParks(Integer indoorCarParks) {
        this.indoorCarParks = indoorCarParks;
        return this;
    }

    public void setIndoorCarParks(Integer indoorCarParks) {
        this.indoorCarParks = indoorCarParks;
    }

    public Integer getOutdoorCarParks() {
        return outdoorCarParks;
    }

    public Listing outdoorCarParks(Integer outdoorCarParks) {
        this.outdoorCarParks = outdoorCarParks;
        return this;
    }

    public void setOutdoorCarParks(Integer outdoorCarParks) {
        this.outdoorCarParks = outdoorCarParks;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public Listing sellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
        return this;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Property getProperty() {
        return property;
    }

    public Listing property(Property property) {
        this.property = property;
        return this;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Set<Offer> getOffers() {
        return offers;
    }

    public Listing offers(Set<Offer> offers) {
        this.offers = offers;
        return this;
    }

    public Listing addOffers(Offer offer) {
        offers.add(offer);
        offer.setFor(this);
        return this;
    }

    public Listing removeOffers(Offer offer) {
        offers.remove(offer);
        offer.setFor(null);
        return this;
    }

    public void setOffers(Set<Offer> offers) {
        this.offers = offers;
    }

    public Person getPerson() {
        return person;
    }

    public Listing person(Person person) {
        this.person = person;
        return this;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Listing listing = (Listing) o;
        if(listing.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, listing.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Listing{" +
            "id=" + id +
            ", minimumPrice='" + minimumPrice + "'" +
            ", builtArea='" + builtArea + "'" +
            ", totalArea='" + totalArea + "'" +
            ", rooms='" + rooms + "'" +
            ", bathrooms='" + bathrooms + "'" +
            ", indoorCarParks='" + indoorCarParks + "'" +
            ", outdoorCarParks='" + outdoorCarParks + "'" +
            ", sellPrice='" + sellPrice + "'" +
            '}';
    }
}
