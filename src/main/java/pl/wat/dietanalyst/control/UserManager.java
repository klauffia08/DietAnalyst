package pl.wat.dietanalyst.control;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wat.dietanalyst.boundary.form.ProfileForm;
import pl.wat.dietanalyst.entity.Role;
import pl.wat.dietanalyst.entity.UserAccount;
import pl.wat.dietanalyst.repository.UserRepository;

import java.util.List;

@Service
public class UserManager {
    private final UserRepository users;
    private final AuditManager audit;

    public UserManager(UserRepository users, AuditManager audit) {
        this.users = users;
        this.audit = audit;
    }

    public UserAccount byEmail(String email) {
        return users.findByEmailIgnoreCase(email).orElseThrow();
    }

    public UserAccount byId(Long id) {
        return users.findById(id).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika."));
    }

    public List<UserAccount> all() {
        return users.findAll();
    }

    public List<UserAccount> dietitians() {
        return users.findAllByRoleOrderByName(Role.DIETITIAN);
    }

    public List<UserAccount> assignedTo(UserAccount dietitian) {
        return users.findAllByAssignedDietitianIdOrderByName(dietitian.getId());
    }

    @Transactional
    public void updateProfile(UserAccount user, ProfileForm form) {
        user.setName(form.getName().trim());
        user.setAge(form.getAge());
        user.setGoal(form.getGoal());
        user.setActivity(form.getActivity());
        user.setCaloriesTarget(form.getCaloriesTarget());
        user.setNotes(form.getNotes());
        users.save(user);
        audit.log(user.getEmail(), "AKTUALIZACJA_PROFILU", user.getName());
    }

    @Transactional
    public void setRole(UserAccount actor, Long id, Role role) {
        UserAccount target = byId(id);
        if (target.getId().equals(actor.getId()) && role != Role.ADMIN) {
            throw new IllegalArgumentException("Administrator nie może odebrać sobie własnej roli.");
        }
        if (target.getRole() == Role.DIETITIAN && role != Role.DIETITIAN) {
            for (UserAccount patient : users.findAllByAssignedDietitianIdOrderByName(target.getId())) {
                patient.setAssignedDietitian(null);
                users.save(patient);
            }
        }
        target.setRole(role);
        if (role != Role.USER) {
            target.setAssignedDietitian(null);
        }
        users.save(target);
        audit.log(actor.getEmail(), "ZMIANA_ROLI", target.getEmail() + " -> " + role);
    }

    @Transactional
    public void toggleActive(UserAccount actor, Long id) {
        UserAccount target = byId(id);
        if (target.getId().equals(actor.getId())) {
            throw new IllegalArgumentException("Nie możesz dezaktywować własnego konta.");
        }
        target.setActive(!target.isActive());
        users.save(target);
        audit.log(actor.getEmail(), "ZMIANA_AKTYWNOSCI", target.getEmail() + " -> " + target.isActive());
    }

    @Transactional
    public void assignDietitian(UserAccount actor, Long userId, Long dietitianId) {
        UserAccount user = byId(userId);
        if (user.getRole() != Role.USER) {
            throw new IllegalArgumentException("Dietetyka można przypisać tylko użytkownikowi.");
        }
        UserAccount dietitian = dietitianId == null ? null : byId(dietitianId);
        if (dietitian != null && dietitian.getRole() != Role.DIETITIAN) {
            throw new IllegalArgumentException("Wybrane konto nie jest dietetykiem.");
        }
        user.setAssignedDietitian(dietitian);
        users.save(user);
        audit.log(actor.getEmail(), "PRZYPISANIE_DIETETYKA",
                user.getEmail() + " -> " + (dietitian == null ? "brak" : dietitian.getEmail()));
    }
}
