package pl.wat.dietanalyst.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import pl.wat.dietanalyst.entity.UserAccount;
import pl.wat.dietanalyst.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository users;
    public CustomUserDetailsService(UserRepository users) { this.users = users; }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = users.findByEmailIgnoreCase(username).orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika."));
        return User.withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .disabled(!user.isActive())
                .build();
    }
}
