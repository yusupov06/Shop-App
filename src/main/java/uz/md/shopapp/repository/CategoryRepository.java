package uz.md.shopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.md.shopapp.domain.Category;
import uz.md.shopapp.dtos.category.CategoryInfoDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdIsNot(String name, Long id);

    @Query(value = "select new uz.md.shopapp.dtos.category.CategoryInfoDto(c.id, c.name, c.description) from Category c where c.deleted = false ")
    List<CategoryInfoDto> findAllForInfo();

    Optional<Category> findByName(String name);
}
