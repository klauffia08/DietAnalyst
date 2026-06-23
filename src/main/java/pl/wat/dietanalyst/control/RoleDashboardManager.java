package pl.wat.dietanalyst.control;

import org.springframework.stereotype.Service;
import pl.wat.dietanalyst.entity.DietPlan;
import pl.wat.dietanalyst.entity.PlanStatus;
import pl.wat.dietanalyst.entity.Role;
import pl.wat.dietanalyst.entity.UserAccount;
import pl.wat.dietanalyst.repository.DietPlanRepository;
import pl.wat.dietanalyst.repository.MealRepository;
import pl.wat.dietanalyst.repository.ProductRepository;
import pl.wat.dietanalyst.repository.UserRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class RoleDashboardManager {
    private final UserRepository users;
    private final MealRepository meals;
    private final DietPlanRepository plans;
    private final ProductRepository products;
    private final RaportManager reports;

    public RoleDashboardManager(UserRepository users, MealRepository meals, DietPlanRepository plans,
                                ProductRepository products, RaportManager reports) {
        this.users = users;
        this.meals = meals;
        this.plans = plans;
        this.products = products;
        this.reports = reports;
    }

    public AdminDashboardStats adminStats() {
        return new AdminDashboardStats(
                users.count(),
                users.countByActiveTrue(),
                users.countByRole(Role.USER),
                users.countByRole(Role.DIETITIAN),
                users.countByRole(Role.ADMIN),
                products.count(),
                meals.count(),
                plans.count(),
                plans.countByStatus(PlanStatus.SUBMITTED)
        );
    }

    public DietitianDashboardStats dietitianStats(UserAccount dietitian) {
        List<UserAccount> patients = users.findAllByAssignedDietitianIdOrderByName(dietitian.getId());
        long pending = 0;
        long approved = 0;
        long recentMeals = 0;
        LocalDate from = LocalDate.now().minusDays(6);
        for (UserAccount patient : patients) {
            pending += plans.countByUserIdAndStatus(patient.getId(), PlanStatus.SUBMITTED);
            approved += plans.countByUserIdAndStatus(patient.getId(), PlanStatus.APPROVED);
            recentMeals += meals.countByUserIdAndDateBetween(patient.getId(), from, LocalDate.now());
        }
        return new DietitianDashboardStats(patients.size(), pending, approved, recentMeals);
    }

    public List<PatientProgress> patientProgress(UserAccount dietitian) {
        LocalDate from = LocalDate.now().minusDays(6);
        return users.findAllByAssignedDietitianIdOrderByName(dietitian.getId()).stream()
                .map(patient -> {
                    List<DietPlan> patientPlans = plans.findAllByUserIdOrderByCreatedAtDesc(patient.getId());
                    String latestStatus = patientPlans.isEmpty() ? "Brak planu" : patientPlans.get(0).getStatus().getLabel();
                    return new PatientProgress(
                            patient,
                            reports.buildSummary(patient),
                            meals.countByUserIdAndDateBetween(patient.getId(), from, LocalDate.now()),
                            meals.countByUserId(patient.getId()),
                            patientPlans.size(),
                            patientPlans.stream().filter(p -> p.getStatus() == PlanStatus.SUBMITTED).count(),
                            latestStatus
                    );
                })
                .toList();
    }

    public List<DietPlan> pendingPlans(UserAccount dietitian) {
        return plans.findAllByUserAssignedDietitianIdAndStatusOrderByCreatedAtAsc(dietitian.getId(), PlanStatus.SUBMITTED);
    }

    public List<UserAccount> newestAccounts() {
        return users.findAll().stream()
                .sorted(Comparator.comparing(UserAccount::getId).reversed())
                .limit(8)
                .toList();
    }
}
