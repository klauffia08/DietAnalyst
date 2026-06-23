package pl.wat.dietanalyst.boundary;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.wat.dietanalyst.control.UserManager;
import pl.wat.dietanalyst.entity.UserAccount;

@ControllerAdvice
public class GlobalModelAdvice {
    private final UserManager users;
    public GlobalModelAdvice(UserManager users) { this.users = users; }

    @ModelAttribute("currentUser")
    public UserAccount currentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) return null;
        return users.byEmail(authentication.getName());
    }
}
