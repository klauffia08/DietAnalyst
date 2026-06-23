package pl.wat.dietanalyst.boundary;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.wat.dietanalyst.boundary.form.MealForm;
import pl.wat.dietanalyst.control.BazaProduktowManager;
import pl.wat.dietanalyst.control.DziennikManager;
import pl.wat.dietanalyst.control.UserManager;
import pl.wat.dietanalyst.entity.MealType;
import pl.wat.dietanalyst.entity.UserAccount;
import java.security.Principal;

@Controller
@RequestMapping("/meals")
public class MealController {
    private final UserManager users; private final DziennikManager diary; private final BazaProduktowManager products;
    public MealController(UserManager users, DziennikManager diary, BazaProduktowManager products) { this.users = users; this.diary = diary; this.products = products; }

    @GetMapping
    public String meals(Principal principal, Model model) {
        UserAccount user = users.byEmail(principal.getName());
        model.addAttribute("meals", diary.getMeals(user));
        model.addAttribute("products", products.findAll());
        model.addAttribute("types", MealType.values());
        model.addAttribute("form", new MealForm());
        return "meals";
    }

    @PostMapping("/add")
    public String add(Principal principal, @Valid @ModelAttribute("form") MealForm form, BindingResult binding, Model model) {
        UserAccount user = users.byEmail(principal.getName());
        if (binding.hasErrors()) { model.addAttribute("meals", diary.getMeals(user)); model.addAttribute("products", products.findAll()); model.addAttribute("types", MealType.values()); return "meals"; }
        diary.addMeal(user, form); return "redirect:/meals?added";
    }

    @PostMapping("/{id}/delete")
    public String delete(Principal principal, @PathVariable Long id) { diary.deleteMeal(users.byEmail(principal.getName()), id); return "redirect:/meals?deleted"; }
}
