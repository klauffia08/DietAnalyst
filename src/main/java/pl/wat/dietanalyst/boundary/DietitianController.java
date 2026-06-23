package pl.wat.dietanalyst.boundary;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.wat.dietanalyst.control.DziennikManager;
import pl.wat.dietanalyst.control.PlanDietyManager;
import pl.wat.dietanalyst.control.RaportManager;
import pl.wat.dietanalyst.control.RoleDashboardManager;
import pl.wat.dietanalyst.control.UserManager;
import pl.wat.dietanalyst.entity.DietPlan;
import pl.wat.dietanalyst.entity.UserAccount;
import pl.wat.dietanalyst.repository.DietPlanRepository;

import java.security.Principal;

@Controller
@RequestMapping("/dietitian")
public class DietitianController {
    private final UserManager users;
    private final DziennikManager diary;
    private final DietPlanRepository plans;
    private final PlanDietyManager planManager;
    private final RaportManager reports;
    private final RoleDashboardManager dashboard;

    public DietitianController(UserManager users, DziennikManager diary, DietPlanRepository plans,
                               PlanDietyManager planManager, RaportManager reports,
                               RoleDashboardManager dashboard) {
        this.users = users;
        this.diary = diary;
        this.plans = plans;
        this.planManager = planManager;
        this.reports = reports;
        this.dashboard = dashboard;
    }

    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {
        UserAccount me = users.byEmail(principal.getName());
        model.addAttribute("stats", dashboard.dietitianStats(me));
        model.addAttribute("patients", dashboard.patientProgress(me));
        model.addAttribute("pendingPlans", dashboard.pendingPlans(me));
        return "dietitian-dashboard";
    }

    @GetMapping({"/patients", "/users"})
    public String patients(Principal principal, Model model) {
        UserAccount me = users.byEmail(principal.getName());
        model.addAttribute("patients", dashboard.patientProgress(me));
        return "dietitian-users";
    }

    @GetMapping({"/patients/{id}", "/users/{id}"})
    public String patient(Principal principal, @PathVariable Long id, Model model) {
        UserAccount me = users.byEmail(principal.getName());
        UserAccount patient = users.byId(id);
        boolean allowed = patient.getAssignedDietitian() != null
                && patient.getAssignedDietitian().getId().equals(me.getId());
        if (!allowed) {
            throw new SecurityException("Brak dostępu do podopiecznego.");
        }
        model.addAttribute("patient", patient);
        model.addAttribute("summary", reports.buildSummary(patient));
        model.addAttribute("meals", diary.getMeals(patient));
        model.addAttribute("plans", plans.findAllByUserIdOrderByCreatedAtDesc(patient.getId()));
        return "dietitian-patient";
    }

    @GetMapping("/plans")
    public String plans(Principal principal, Model model) {
        UserAccount me = users.byEmail(principal.getName());
        model.addAttribute("plans", dashboard.pendingPlans(me));
        return "dietitian-plans";
    }

    @PostMapping("/plans/{id}/review")
    public String review(Principal principal, @PathVariable Long id,
                         @RequestParam boolean approve,
                         @RequestParam(defaultValue = "") String note) {
        UserAccount me = users.byEmail(principal.getName());
        DietPlan plan = plans.findById(id).orElseThrow();
        planManager.review(me, id, approve, note);
        return "redirect:/dietitian/patients/" + plan.getUser().getId() + "?reviewed";
    }
}
