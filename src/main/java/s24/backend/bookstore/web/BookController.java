package s24.backend.bookstore.web;
import s24.backend.bookstore.domain.Book;

import s24.backend.bookstore.domain.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BookController {
    @Autowired
    private BookRepository repository;
    @RequestMapping("/index")
    public String showIndex() {
        return "index";
    }

    @RequestMapping(value={"/", "/booklist"})
    public String showList(Model model) {
        model.addAttribute("books", repository.findAll());
        return "booklist";
    }

    @RequestMapping("/add")
    public String addbook(Model model){
        model.addAttribute("book", new Book());
        return "addbook";
    }
    @PostMapping("/save")
    public String save(Book book){
        repository.save(book);
        return "redirect:booklist";
    }
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id, Model model) {
        repository.deleteById(id);
        return "redirect:/booklist";
    }
    
    @RequestMapping("/edit/{id}")
    public String showEditBook(@PathVariable("id") Long id, Model model){
        model.addAttribute("book", repository.findById(id));
        return "editbook";
    }
}
