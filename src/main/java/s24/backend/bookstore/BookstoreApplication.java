package s24.backend.bookstore;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import s24.backend.bookstore.domain.AppUser;
import s24.backend.bookstore.domain.AppUserRepository;
import s24.backend.bookstore.domain.Category;
import s24.backend.bookstore.domain.CategoryRepository;

@SpringBootApplication
public class BookstoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

	@Bean
	@Profile("!test")
	public CommandLineRunner demo(CategoryRepository categoryRepository) {
		return (args) -> {
			categoryRepository.save(new Category("Fiction"));
			categoryRepository.save(new Category("Non-Fiction"));
			categoryRepository.save(new Category("Fantasy"));
			categoryRepository.save(new Category("Science"));
		};
	}
	@Bean
	@Profile("!test")
    public CommandLineRunner initUsers(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            appUserRepository.save(new AppUser("user", passwordEncoder.encode("password"), "user@example.com", "USER"));
            appUserRepository.save(new AppUser("admin", passwordEncoder.encode("admin"), "admin@example.com", "ADMIN"));
        };
    }
}
