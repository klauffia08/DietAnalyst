package pl.wat.dietanalyst.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wat.dietanalyst.entity.AuditLog;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findTop100ByOrderByEventTimeDesc();
}
