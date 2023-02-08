package uz.md.shopapp.repository;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.md.shopapp.domain.AccessKey;

import java.util.Optional;

public interface AccessKeyRepository extends JpaRepository<AccessKey, Long> {
    Optional<AccessKey> findByAccess(String access);
}
