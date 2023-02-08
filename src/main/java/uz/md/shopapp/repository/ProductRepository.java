package uz.md.shopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.md.shopapp.domain.Order;
import uz.md.shopapp.domain.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdIsNot(String name, Long id);

    List<Product> findAllByCategory_Id(Long category_id);

}
