package pl.wat.dietanalyst.boundary;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.wat.dietanalyst.boundary.form.RegistrationForm;
import pl.wat.dietanalyst.control.AutoryzacjaManager;

@Controller
public class AuthController {
    private final AutoryzacjaManager auth;
    public AuthController(AutoryzacjaManager auth) { this.auth = auth; }

    @GetMapping("/") public String home() { return "redirect:/dashboard"; }
    @GetMapping("/login") public String login() { return "login"; }

    @GetMapping("/register")
    public String register(Model model) { model.addAttribute("form", new RegistrationForm()); return "register"; }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("form") RegistrationForm form, BindingResult binding, Model model) {
        if (binding.hasErrors()) return "register";
        try {
            auth.register(form);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage()); return "register";
        }
    }
}
