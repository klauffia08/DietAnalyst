package pl.wat.dietanalyst.boundary;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.wat.dietanalyst.control.RaportManager;
import pl.wat.dietanalyst.control.UserManager;
import pl.wat.dietanalyst.entity.UserAccount;
import java.security.Principal;

@Controller
public class ReportController {
    private final UserManager users; private final RaportManager reports;
    public ReportController(UserManager users, RaportManager reports) { this.users = users; this.reports = reports; }
    @GetMapping("/reports") public String reports(Principal principal, Model model) { UserAccount u = users.byEmail(principal.getName()); model.addAttribute("summary", reports.buildSummary(u)); return "reports"; }
}
