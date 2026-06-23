package pl.wat.dietanalyst.control;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wat.dietanalyst.entity.*;
import pl.wat.dietanalyst.interfaces.IPlanDiety;
import pl.wat.dietanalyst.repository.DietPlanRepository;
import pl.wat.dietanalyst.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlanDietyManager implements IPlanDiety {
    private final DietPlanRepository plans;
    private final ProductRepository products;
    private final AuditManager audit;

    public PlanDietyManager(DietPlanRepository plans, ProductRepository products, AuditManager audit) {
        this.plans = plans; this.products = products; this.audit = audit;
    }

    @Override
    @Transactional
    public DietPlan generate(UserAccount user) {
        List<Product> all = products.findAllByOrderByNameAsc();
        String breakfast = find(all, "Płatki owsiane", "Jogurt naturalny");
        String lunch = find(all, "Pierś z kurczaka", "Ryż biały");
        String dinner = find(all, "Jajko", "Awokado");
        String content = """
                DZIEŃ 1
                Śniadanie: %s + owoc
                Obiad: %s + warzywa
                Kolacja: %s

                DZIEŃ 2
                Śniadanie: jogurt naturalny + płatki owsiane
                Obiad: pierś z kurczaka + ryż + sałatka
                Kolacja: jajka + warzywa

                DZIEŃ 3
                Śniadanie: owsianka z owocami
                Obiad: źródło białka + produkt zbożowy + warzywa
                Kolacja: lekki posiłek białkowy
                """.formatted(breakfast, lunch, dinner);
        DietPlan plan = new DietPlan();
        plan.setName("Plan – " + safe(user.getGoal()));
        plan.setStatus(PlanStatus.GENERATED);
        plan.setCreatedAt(LocalDateTime.now());
        plan.setContent(content);
        plan.setUser(user);
        DietPlan saved = plans.save(plan);
        audit.log(user.getEmail(), "GENEROWANIE_PLANU", saved.getName());
        return saved;
    }

    private String find(List<Product> all, String first, String second) {
        boolean hasFirst = all.stream().anyMatch(p -> p.getName().equalsIgnoreCase(first));
        boolean hasSecond = all.stream().anyMatch(p -> p.getName().equalsIgnoreCase(second));
        if (hasFirst && hasSecond) return first + " + " + second;
        return all.isEmpty() ? "zbilansowany posiłek" : all.get(0).getName();
    }
    private String safe(String value) { return value == null || value.isBlank() ? "zdrowe odżywianie" : value; }

    @Override public List<DietPlan> getPlans(UserAccount user) { return plans.findAllByUserIdOrderByCreatedAtDesc(user.getId()); }

    @Override @Transactional public void submit(UserAccount user, Long planId) {
        DietPlan plan = owned(user, planId); plan.setStatus(PlanStatus.SUBMITTED); plans.save(plan);
        audit.log(user.getEmail(), "WYSŁANIE_PLANU", String.valueOf(planId));
    }
    @Override @Transactional public void archive(UserAccount user, Long planId) {
        DietPlan plan = owned(user, planId); plan.setStatus(PlanStatus.ARCHIVED); plans.save(plan);
        audit.log(user.getEmail(), "ARCHIWIZACJA_PLANU", String.valueOf(planId));
    }
    private DietPlan owned(UserAccount user, Long id) {
        DietPlan p = plans.findById(id).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono planu."));
        if (!p.getUser().getId().equals(user.getId())) throw new SecurityException("Brak dostępu do planu.");
        return p;
    }

    @Transactional
    public void review(UserAccount reviewer, Long planId, boolean approve, String note) {
        DietPlan p = plans.findById(planId).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono planu."));
        boolean assigned = reviewer.getRole() == Role.DIETITIAN
                && p.getUser().getAssignedDietitian() != null
                && p.getUser().getAssignedDietitian().getId().equals(reviewer.getId());
        if (!assigned) throw new SecurityException("Plan nie należy do przypisanego podopiecznego.");
        p.setStatus(approve ? PlanStatus.APPROVED : PlanStatus.REJECTED);
        p.setDietitianNote(note);
        p.setReviewedBy(reviewer);
        plans.save(p);
        audit.log(reviewer.getEmail(), approve ? "ZATWIERDZENIE_PLANU" : "ODRZUCENIE_PLANU", "plan=" + planId);
    }
}
