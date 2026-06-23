package pl.wat.dietanalyst.boundary;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.wat.dietanalyst.boundary.form.ProductForm;
import pl.wat.dietanalyst.control.BazaProduktowManager;

import java.security.Principal;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final BazaProduktowManager products;

    public ProductController(BazaProduktowManager products) {
        this.products = products;
    }

    @GetMapping
    public String products(Model model) {
        model.addAttribute("products", products.findAll());
        model.addAttribute("form", new ProductForm());
        return "products";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('DIETITIAN','ADMIN')")
    public String add(Principal principal, @Valid @ModelAttribute("form") ProductForm form,
                      BindingResult binding, Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("products", products.findAll());
            return "products";
        }
        try {
            products.add(form, principal.getName());
            return "redirect:/products?added";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("products", products.findAll());
            model.addAttribute("error", ex.getMessage());
            return "products";
        }
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyRole('DIETITIAN','ADMIN')")
    public String update(Principal principal, @PathVariable Long id, ProductForm form,
                         RedirectAttributes redirect) {
        try {
            products.update(id, form, principal.getName());
            redirect.addAttribute("updated", true);
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/products";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyRole('DIETITIAN','ADMIN')")
    public String delete(Principal principal, @PathVariable Long id, RedirectAttributes redirect) {
        try {
            products.delete(id, principal.getName());
            redirect.addAttribute("deleted", true);
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/products";
    }
}
