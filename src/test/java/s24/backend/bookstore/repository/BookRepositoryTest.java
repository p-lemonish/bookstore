package s24.backend.bookstore.repository;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import s24.backend.bookstore.domain.Book;
import s24.backend.bookstore.domain.BookRepository;
import s24.backend.bookstore.domain.Category;
import s24.backend.bookstore.domain.CategoryRepository;

@DataJpaTest
@ActiveProfiles("test")
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testCreateBook() {
        Category category = new Category("testCategory");
        categoryRepository.save(category);
        Book book = new Book("bookname", "bookauthor", 2022, "123123123", 3.50, category);
        bookRepository.save(book);

        assertThat(book.getId()).isNotNull();
    }
    
    @Test
    public void testDeleteBook() {
        Category category = new Category("testCategory");
        categoryRepository.save(category);
        Book book = new Book("bookname", "bookauthor", 2022, "123123123", 3.50, category);
        bookRepository.save(book);

        Long bookId = book.getId();
        bookRepository.delete(book);

        assertThat(bookRepository.findById(bookId)).isEmpty();
    }

    @Test
    public void testUpdateBook() {
        Category category = new Category("testCategory");
        categoryRepository.save(category);
        Book book = new Book("bookname", "bookauthor", 2022, "123123123", 3.50, category);
        bookRepository.save(book);
        book.setTitle("updatedtitle");
        bookRepository.save(book);

        Book updatedBook = bookRepository.findById(book.getId()).orElse(null);
        assertThat(updatedBook).isNotNull();
        assertThat(updatedBook.getTitle()).isEqualTo("updatedtitle");
    }

    @Test
    public void testFindBookById() {
        Category category = new Category("testCategory");
        categoryRepository.save(category);
        Book book = new Book("bookname", "bookauthor", 2022, "123123123", 3.50, category);
        bookRepository.save(book);
        Long bookId = book.getId();

        Book foundBook = bookRepository.findById(bookId).orElse(null);
        assertThat(foundBook).isNotNull();
        assertThat(foundBook.getTitle()).isEqualTo("bookname");
    }

    @Test
    public void testFindBookByTitle() {
        Category category = new Category("testCategory");
        categoryRepository.save(category);
        Book book = new Book("bookname", "bookauthor", 2022, "123123123", 3.50, category);
        bookRepository.save(book);

        Book foundBook = bookRepository.findByTitle("bookname");
        assertThat(foundBook).isNotNull();
        assertThat(foundBook.getIsbn()).isEqualTo("123123123");
    }

    @Test
    public void testFindAllBooks() {
        Category category = new Category("testCategory");
        categoryRepository.save(category);
        Book book = new Book("bookname", "bookauthor", 2022, "123123123", 3.50, category);
        Book book2 = new Book("bookname2", "bookauthor2", 2023, "1231523123", 3.50, category);
        bookRepository.save(book);
        bookRepository.save(book2);

        List<Book> books = bookRepository.findAll();

        assertThat(books).hasSize(2);
    }
    
}
