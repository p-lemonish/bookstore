package s24.backend.bookstore.web;
import s24.backend.bookstore.domain.Book;

import s24.backend.bookstore.domain.BookRepository;
import s24.backend.bookstore.domain.CategoryRepository;
import s24.backend.bookstore.domain.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @RequestMapping("/index")
    public String showIndex() {
        return "index";
    }

    @RequestMapping(value={"/", "/booklist"})
    public String showList(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "booklist";
    }
    @PostMapping("/save")
    public String save(Book book, @RequestParam("category") Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->  new IllegalArgumentException("Invalid category ID: " + categoryId));
        book.setCategory(category);
        bookRepository.save(book);
        return "redirect:/booklist";
    }
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id, Model model) {
        bookRepository.deleteById(id);
        return "redirect:/booklist";
    }
    
    @RequestMapping("/edit/{id}")
    public String showEditBook(@PathVariable("id") Long id, Model model){
        model.addAttribute("book", bookRepository.findById(id));
        return "editbook";
    }

    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryRepository.findAll());
        return "addbook";
    }
}
