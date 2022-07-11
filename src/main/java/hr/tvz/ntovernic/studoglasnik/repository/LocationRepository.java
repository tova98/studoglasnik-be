package hr.tvz.ntovernic.studoglasnik.repository;

import hr.tvz.ntovernic.studoglasnik.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
