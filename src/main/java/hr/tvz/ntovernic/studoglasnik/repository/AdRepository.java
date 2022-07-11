package hr.tvz.ntovernic.studoglasnik.repository;

import hr.tvz.ntovernic.studoglasnik.dto.Filter;
import hr.tvz.ntovernic.studoglasnik.model.Ad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {

    @Query("""
        select ad from Ad ad where
        (:#{#filter.title} is null or lower(ad.title) like %:#{#filter.title}%) and
        (:#{#filter.category} is null or ad.category = :#{#filter.category}) and
        (:#{#filter.location} is null or ad.location = :#{#filter.location}) and
        (:#{#filter.priceFrom} is null or ad.price >= :#{#filter.priceFrom}) and
        (:#{#filter.priceTo} is null or ad.price <= :#{#filter.priceTo}) and
        ((ad.expireDate > current_timestamp) or
        (:#{#filter.expired} = true and ad.expireDate < current_timestamp ))
        """)
    Page<Ad> findAllFiltered(@Param("filter") Filter filter, Pageable pageable);

    @Query("""
        select count(ad) from Ad ad where
        (:#{#filter.title} is null or lower(ad.title) like %:#{#filter.title}%) and
        (:#{#filter.category} is null or ad.category = :#{#filter.category}) and
        (:#{#filter.location} is null or ad.location = :#{#filter.location}) and
        (:#{#filter.priceFrom} is null or ad.price >= :#{#filter.priceFrom}) and
        (:#{#filter.priceTo} is null or ad.price <= :#{#filter.priceTo}) and
        ((ad.expireDate > current_timestamp) or
        (:#{#filter.expired} = true and ad.expireDate < current_timestamp ))
        """)
    Integer countAllFiltered(@Param("filter") Filter filter);

    @Query("""
        select ad from Ad ad where
        (ad.contactUser.id = :userId) and
        (:#{#filter.title} is null or lower(ad.title) like %:#{#filter.title}%) and
        (:#{#filter.category} is null or ad.category = :#{#filter.category}) and
        (:#{#filter.location} is null or ad.location = :#{#filter.location}) and
        (:#{#filter.priceFrom} is null or ad.price >= :#{#filter.priceFrom}) and
        (:#{#filter.priceTo} is null or ad.price <= :#{#filter.priceTo}) and
        ((ad.expireDate > current_timestamp) or
        (:#{#filter.expired} = true and ad.expireDate < current_timestamp ))
        """)
    Page<Ad> findAllFilteredByUser(Long userId, @Param("filter") Filter filter, Pageable pageable);

    @Query("""
        select count(ad) from Ad ad where
        (ad.contactUser.id = :userId) and
        (:#{#filter.title} is null or lower(ad.title) like %:#{#filter.title}%) and
        (:#{#filter.category} is null or ad.category = :#{#filter.category}) and
        (:#{#filter.location} is null or ad.location = :#{#filter.location}) and
        (:#{#filter.priceFrom} is null or ad.price >= :#{#filter.priceFrom}) and
        (:#{#filter.priceTo} is null or ad.price <= :#{#filter.priceTo}) and
        ((ad.expireDate > current_timestamp) or
        (:#{#filter.expired} = true and ad.expireDate < current_timestamp ))
        """)
    Integer countAllFilteredByUser(Long userId, @Param("filter") Filter filter);
}
