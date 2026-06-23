package pl.wat.dietanalyst.control;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wat.dietanalyst.boundary.form.RegistrationForm;
import pl.wat.dietanalyst.entity.Role;
import pl.wat.dietanalyst.entity.UserAccount;
import pl.wat.dietanalyst.interfaces.IAutoryzacja;
import pl.wat.dietanalyst.repository.UserRepository;

@Service
public class AutoryzacjaManager implements IAutoryzacja {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuditManager audit;

    public AutoryzacjaManager(UserRepository users, PasswordEncoder encoder, AuditManager audit) {
        this.users = users; this.encoder = encoder; this.audit = audit;
    }

    @Override
    @Transactional
    public UserAccount register(RegistrationForm form) {
        String email = form.getEmail().trim().toLowerCase();
        if (users.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Konto o tym adresie e-mail już istnieje.");
        }
        UserAccount user = new UserAccount();
        user.setName(form.getName().trim());
        user.setEmail(email);
        user.setPassword(encoder.encode(form.getPassword()));
        user.setRole(Role.USER);
        user.setActive(true);
        user.setCaloriesTarget(2000);
        UserAccount saved = users.save(user);
        audit.log(email, "REJESTRACJA", "Utworzono nowe konto użytkownika");
        return saved;
    }
}
