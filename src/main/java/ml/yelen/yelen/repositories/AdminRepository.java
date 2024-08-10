package ml.yelen.yelen.repositories;

import ml.yelen.yelen.entities.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Administrator, Long> {
    Optional<Administrator> findByUsername(String username);
}
