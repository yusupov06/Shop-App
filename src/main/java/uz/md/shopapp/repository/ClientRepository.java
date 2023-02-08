package uz.md.shopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.md.shopapp.domain.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByUsername(String username);
}
