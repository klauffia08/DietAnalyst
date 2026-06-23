package pl.wat.dietanalyst.boundary;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.wat.dietanalyst.boundary.form.ProfileForm;
import pl.wat.dietanalyst.control.UserManager;
import pl.wat.dietanalyst.entity.UserAccount;
import java.security.Principal;

@Controller
public class ProfileController {
    private final UserManager users;
    public ProfileController(UserManager users) { this.users = users; }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        UserAccount u = users.byEmail(principal.getName());
        ProfileForm f = new ProfileForm(); f.setName(u.getName()); f.setEmail(u.getEmail()); f.setAge(u.getAge()); f.setGoal(u.getGoal()); f.setActivity(u.getActivity()); f.setCaloriesTarget(u.getCaloriesTarget()); f.setNotes(u.getNotes());
        model.addAttribute("form", f); return "profile";
    }

    @PostMapping("/profile")
    public String update(Principal principal, @Valid @ModelAttribute("form") ProfileForm form, BindingResult binding, Model model) {
        UserAccount u = users.byEmail(principal.getName());
        if (binding.hasErrors()) return "profile";
        try { users.updateProfile(u, form); return "redirect:/profile?saved"; }
        catch (IllegalArgumentException ex) { model.addAttribute("error", ex.getMessage()); return "profile"; }
    }
}
