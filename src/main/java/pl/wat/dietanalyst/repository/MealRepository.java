package pl.wat.dietanalyst.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wat.dietanalyst.entity.Meal;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findAllByUserIdOrderByDateDescIdDesc(Long userId);
    List<Meal> findAllByUserIdAndDate(Long userId, LocalDate date);
    long countByUserId(Long userId);
    long countByProductId(Long productId);
    long countByUserIdAndDateBetween(Long userId, LocalDate from, LocalDate to);
}
