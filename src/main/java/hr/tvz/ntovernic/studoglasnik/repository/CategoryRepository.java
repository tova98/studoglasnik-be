package hr.tvz.ntovernic.studoglasnik.repository;

import hr.tvz.ntovernic.studoglasnik.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
