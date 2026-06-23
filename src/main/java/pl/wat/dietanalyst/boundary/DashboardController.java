package pl.wat.dietanalyst.boundary;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.wat.dietanalyst.control.RaportManager;
import pl.wat.dietanalyst.control.UserManager;
import pl.wat.dietanalyst.entity.Role;
import pl.wat.dietanalyst.entity.UserAccount;

import java.security.Principal;

@Controller
public class DashboardController {
    private final UserManager users;
    private final RaportManager reports;

    public DashboardController(UserManager users, RaportManager reports) {
        this.users = users;
        this.reports = reports;
    }

    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {
        UserAccount user = users.byEmail(principal.getName());
        if (user.getRole() == Role.ADMIN) {
            return "redirect:/admin/dashboard";
        }
        if (user.getRole() == Role.DIETITIAN) {
            return "redirect:/dietitian/dashboard";
        }
        model.addAttribute("summary", reports.buildSummary(user));
        return "dashboard";
    }
}
