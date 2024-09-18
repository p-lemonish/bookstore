package s24.backend.bookstore.web;
import s24.backend.bookstore.domain.Book;

import s24.backend.bookstore.domain.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/api/books")
public class RestBookController {
    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    @PostMapping
    public Book addBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }
    
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable long id) {
        bookRepository.deleteById(id);
    }

    @GetMapping("/{id}")
    public Optional<Book> getBookById(@PathVariable long id) {
        return bookRepository.findById(id);
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        return bookRepository.findById(id).map(book -> {
                book.setTitle(updatedBook.getTitle());
                book.setAuthor(updatedBook.getAuthor());
                book.setPublicationYear(updatedBook.getPublicationYear());
                book.setIsbn(updatedBook.getIsbn());
                book.setPrice(updatedBook.getPrice());
                book.setCategory(updatedBook.getCategory());
                return bookRepository.save(book);
            })
            .orElseThrow(() -> new IllegalArgumentException("Invalid book ID: " + id));
    }
    
}
