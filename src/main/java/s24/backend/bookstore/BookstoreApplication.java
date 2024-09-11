package s24.backend.bookstore;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import s24.backend.bookstore.domain.Category;
import s24.backend.bookstore.domain.CategoryRepository;

@SpringBootApplication
public class BookstoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(CategoryRepository categoryRepository) {
		return (args) -> {
			categoryRepository.save(new Category("Fiction"));
			categoryRepository.save(new Category("Non-Fiction"));
			categoryRepository.save(new Category("Fantasy"));
			categoryRepository.save(new Category("Science"));
		};
	}
}
