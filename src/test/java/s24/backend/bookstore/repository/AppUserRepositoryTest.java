package s24.backend.bookstore.repository;


import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import s24.backend.bookstore.domain.AppUser;
import s24.backend.bookstore.domain.AppUserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
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

    }

    @Test
    public void testFindUserByUsername() {}

    @Test
    public void testFindAllUsers() {}
}
