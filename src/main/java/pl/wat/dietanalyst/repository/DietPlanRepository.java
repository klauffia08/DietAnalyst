package pl.wat.dietanalyst.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wat.dietanalyst.entity.DietPlan;
import pl.wat.dietanalyst.entity.PlanStatus;

import java.util.List;

public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {
    List<DietPlan> findAllByUserIdOrderByCreatedAtDesc(Long userId);
    List<DietPlan> findAllByUserAssignedDietitianIdOrderByCreatedAtDesc(Long dietitianId);
    List<DietPlan> findAllByUserAssignedDietitianIdAndStatusOrderByCreatedAtAsc(Long dietitianId, PlanStatus status);
    long countByStatus(PlanStatus status);
    long countByUserIdAndStatus(Long userId, PlanStatus status);
}
