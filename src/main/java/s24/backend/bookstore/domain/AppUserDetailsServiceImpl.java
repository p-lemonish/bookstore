package s24.backend.bookstore.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsServiceImpl implements UserDetailsService {
    private final AppUserRepository repository;
    public AppUserDetailsServiceImpl(AppUserRepository repository) {
        this.repository = repository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = repository.findByUsername(username);
        if (appUser == null) {
            throw new UsernameNotFoundException("User not found");
        }

        UserBuilder builder = User.withUsername(username);
        builder.password(appUser.getPassword());
        builder.roles(appUser.getRole());
        return builder.build();
    }
}