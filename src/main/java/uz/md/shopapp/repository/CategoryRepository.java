package uz.md.shopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.md.shopapp.domain.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdIsNot(String name, Long id);

    @Query("from Category where deleted = false")
    List<Category> findAllForInfo();

    Optional<Category> findByName(String name);
}
