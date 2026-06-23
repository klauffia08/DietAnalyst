package pl.wat.dietanalyst.boundary;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.wat.dietanalyst.control.AuditManager;
import pl.wat.dietanalyst.control.RoleDashboardManager;
import pl.wat.dietanalyst.control.UserManager;
import pl.wat.dietanalyst.entity.Role;
import pl.wat.dietanalyst.entity.UserAccount;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserManager users;
    private final AuditManager audit;
    private final RoleDashboardManager dashboard;

    public AdminController(UserManager users, AuditManager audit, RoleDashboardManager dashboard) {
        this.users = users;
        this.audit = audit;
        this.dashboard = dashboard;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("stats", dashboard.adminStats());
        model.addAttribute("newestUsers", dashboard.newestAccounts());
        model.addAttribute("logs", audit.latest().stream().limit(8).toList());
        return "admin-dashboard";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", users.all());
        model.addAttribute("dietitians", users.dietitians());
        model.addAttribute("roles", Role.values());
        return "admin-users";
    }

    @PostMapping("/users/{id}/role")
    public String role(Principal principal, @PathVariable Long id, @RequestParam Role role,
                       RedirectAttributes redirect) {
        try {
            users.setRole(users.byEmail(principal.getName()), id, role);
            redirect.addAttribute("roleChanged", true);
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/toggle")
    public String toggle(Principal principal, @PathVariable Long id, RedirectAttributes redirect) {
        try {
            users.toggleActive(users.byEmail(principal.getName()), id);
            redirect.addAttribute("statusChanged", true);
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/dietitian")
    public String assign(Principal principal, @PathVariable Long id,
                         @RequestParam(required = false) Long dietitianId,
                         RedirectAttributes redirect) {
        try {
            users.assignDietitian(users.byEmail(principal.getName()), id, dietitianId);
            redirect.addAttribute("assigned", true);
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/audit")
    public String audit(Model model) {
        model.addAttribute("logs", audit.latest());
        return "admin-audit";
    }
}
