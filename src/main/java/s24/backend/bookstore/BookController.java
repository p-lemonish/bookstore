package s24.backend.bookstore;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BookController {
    @RequestMapping("/index")
    public String showIndex() {
        return "index";
    }
    
}
