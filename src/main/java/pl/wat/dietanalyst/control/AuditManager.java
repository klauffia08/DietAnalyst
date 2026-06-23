package pl.wat.dietanalyst.control;

import org.springframework.stereotype.Service;
import pl.wat.dietanalyst.entity.AuditLog;
import pl.wat.dietanalyst.repository.AuditLogRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditManager {
    private final AuditLogRepository repository;
    public AuditManager(AuditLogRepository repository) { this.repository = repository; }

    public void log(String actorEmail, String action, String details) {
        AuditLog log = new AuditLog();
        log.setEventTime(LocalDateTime.now());
        log.setActorEmail(actorEmail);
        log.setAction(action);
        log.setDetails(details);
        repository.save(log);
    }

    public List<AuditLog> latest() { return repository.findTop100ByOrderByEventTimeDesc(); }
}
