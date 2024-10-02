package s24.backend.bookstore.repository;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import s24.backend.bookstore.domain.Category;
import s24.backend.bookstore.domain.CategoryRepository;

@DataJpaTest
@ActiveProfiles("test")
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testCreateCategory() {
        Category category = new Category("testCategory");
        categoryRepository.save(category);

        assertThat(category.getId()).isNotNull();
    }
    
    @Test
    public void testDeleteCategory() {
        Category category = new Category("testCategory");
        categoryRepository.save(category);

        Long categoryId = category.getId();
        categoryRepository.delete(category);

        assertThat(categoryRepository.findById(categoryId)).isEmpty();
    }

    @Test
    public void testUpdateCategory() {
        Category category = new Category("testCategory");
        categoryRepository.save(category);

        category.setName("updatedCategory");
        categoryRepository.save(category);

        Category updatedCategory = categoryRepository.findById(category.getId()).orElse(null);
        assertThat(updatedCategory).isNotNull();
        assertThat(updatedCategory.getName()).isEqualTo("updatedCategory");
    }

    @Test
    public void testFindCategoryById() {
        Category category = new Category("testCategory");
        categoryRepository.save(category);
        Long categoryId = category.getId();

        Category foundCategory = categoryRepository.findById(categoryId).orElse(null);
        assertThat(foundCategory).isNotNull();
        assertThat(foundCategory.getName()).isEqualTo("testCategory");
    }

    @Test
    public void testFindAllCategories() {
        Category category = new Category("testCategory");
        Category category2 = new Category("test2Category");
        categoryRepository.save(category);
        categoryRepository.save(category2);

        List<Category> categories = categoryRepository.findAll();
        assertThat(categories).hasSize(2);
    }
    
}
