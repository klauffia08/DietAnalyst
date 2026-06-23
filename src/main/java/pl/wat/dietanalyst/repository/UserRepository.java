package pl.wat.dietanalyst.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wat.dietanalyst.entity.Role;
import pl.wat.dietanalyst.entity.UserAccount;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    List<UserAccount> findAllByRoleOrderByName(Role role);
    List<UserAccount> findAllByAssignedDietitianIdOrderByName(Long dietitianId);
    long countByRole(Role role);
    long countByActiveTrue();
}
