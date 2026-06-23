package pl.wat.dietanalyst.boundary;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.wat.dietanalyst.control.PlanDietyManager;
import pl.wat.dietanalyst.control.UserManager;
import pl.wat.dietanalyst.entity.UserAccount;
import java.security.Principal;

@Controller
@RequestMapping("/plans")
public class PlanController {
    private final UserManager users; private final PlanDietyManager plans;
    public PlanController(UserManager users, PlanDietyManager plans) { this.users = users; this.plans = plans; }

    @GetMapping public String plans(Principal principal, Model model) { UserAccount u = users.byEmail(principal.getName()); model.addAttribute("plans", plans.getPlans(u)); return "plans"; }
    @PostMapping("/generate") public String generate(Principal principal) { plans.generate(users.byEmail(principal.getName())); return "redirect:/plans?generated"; }
    @PostMapping("/{id}/submit") public String submit(Principal principal, @PathVariable Long id) { plans.submit(users.byEmail(principal.getName()), id); return "redirect:/plans?submitted"; }
    @PostMapping("/{id}/archive") public String archive(Principal principal, @PathVariable Long id) { plans.archive(users.byEmail(principal.getName()), id); return "redirect:/plans?archived"; }
}
