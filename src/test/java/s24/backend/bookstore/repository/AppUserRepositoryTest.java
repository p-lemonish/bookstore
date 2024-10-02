package s24.backend.bookstore.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import s24.backend.bookstore.domain.AppUser;
import s24.backend.bookstore.domain.AppUserRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class AppUserRepositoryTest {
    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    public void testCreateUser() {
        AppUser appUser = new AppUser("name", "password", "email@example.com", "USER");
        appUserRepository.save(appUser);

        assertThat(appUser.getId()).isNotNull();
    }
    
    @Test
    public void testDeleteUser() {
        AppUser appUser = new AppUser("name", "password", "email@example.com", "USER");
        appUserRepository.save(appUser);

        Long userId = appUser.getId();
        appUserRepository.delete(appUser);
        assertThat(appUserRepository.findById(userId)).isEmpty();
    }

    @Test
    public void testUpdateUser() {
        AppUser appUser = new AppUser("name", "password", "email@example.com", "USER");
        appUserRepository.save(appUser);

        appUser.setUsername("newname");
        appUserRepository.save(appUser);

        AppUser updatedUser = appUserRepository.findById(appUser.getId()).orElse(null);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getUsername()).isEqualTo("newname");
    }

    @Test
    public void testFindUserById() {
        AppUser appUser = new AppUser("name", "password", "email@example.com", "USER");
        appUserRepository.save(appUser);

        Long userId = appUser.getId();
        AppUser foundUser = appUserRepository.findById(userId).orElse(null);
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("name");
    }

    @Test
    public void testFindUserByUsername() {
        AppUser appUser = new AppUser("name", "password", "email@example.com", "USER");
        appUserRepository.save(appUser);

        AppUser foundUser = appUserRepository.findByUsername("name");
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("email@example.com");
    }

    @Test
    public void testFindAllUsers() {
        AppUser appUser = new AppUser("name", "password", "email@example.com", "USER");
        AppUser appUser2 = new AppUser("name2", "password2", "email2@example2.com2", "USER");
        appUserRepository.save(appUser);
        appUserRepository.save(appUser2);

        List<AppUser> users = appUserRepository.findAll();
        assertThat(users).hasSize(2);
    }
}
