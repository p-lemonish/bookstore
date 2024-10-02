package s24.backend.bookstore.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import s24.backend.bookstore.domain.AppUserRepository;
import s24.backend.bookstore.domain.Book;
import s24.backend.bookstore.domain.BookRepository;
import s24.backend.bookstore.domain.CategoryRepository;
import s24.backend.bookstore.exception.GlobalExceptionHandler;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@WebMvcTest(RestBookController.class)
@Import(GlobalExceptionHandler.class)
public class RestBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;
    
    @MockBean
    private AppUserRepository appUserRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @Test
    @WithMockUser
    public void testGetAllBooks() throws Exception {
        Book book1 = new Book("Book Title 1", "Author 1", 2001, "123456789", 20);
        book1.setId(1L);
        Book book2 = new Book("Book Title 2", "Author 2", 2002, "987654321", 25);
        book2.setId(2L);

        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        mockMvc.perform(get("/api/books")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Book Title 1"))
                .andExpect(jsonPath("$[1].title").value("Book Title 2"));
    }

    @Test
    @WithMockUser
    public void testGetBookById() throws Exception {
        Book book = new Book("Book Title", "Author", 2000, "1234567890", 30);
        book.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/1")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Book Title"))
                .andExpect(jsonPath("$.author").value("Author"))
                .andExpect(jsonPath("$.price").value(30));
    }

    @Test
    @WithMockUser
    public void testGetBookById_NotFound() throws Exception {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/999")
                .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testAddBook() throws Exception {
        Book newBook = new Book("New Book", "New Author", 2021, "1122334455", 40);
        String newBookJson = "{ \"title\": \"New Book\", \"author\": \"New Author\", \"publicationYear\": 2021, \"isbn\": \"1122334455\", \"price\": 40 }";

        when(bookRepository.save(any(Book.class))).thenReturn(newBook);

        mockMvc.perform(post("/api/books")
                .with(csrf())
                .contentType(APPLICATION_JSON)
                .content(newBookJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Book"))
                .andExpect(jsonPath("$.author").value("New Author"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testAddBook_InvalidData() throws Exception {
        String invalidBookJson = "{ \"title\": \"\", \"author\": \"\", \"publicationYear\": -1, \"isbn\": \"\", \"price\": -10 }";

        mockMvc.perform(post("/api/books")
                .with(csrf())
                .contentType(APPLICATION_JSON)
                .content(invalidBookJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testUpdateBook() throws Exception {
        Book existingBook = new Book("Old Title", "Old Author", 2000, "1234567890", 30);
        existingBook.setId(1L);

        Book updatedBook = new Book("Updated Title", "Updated Author", 2022, "0987654321", 35);
        updatedBook.setId(1L);

        String updatedBookJson = "{ \"title\": \"Updated Title\", \"author\": \"Updated Author\", \"publicationYear\": 2022, \"isbn\": \"0987654321\", \"price\": 35 }";

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/api/books/1")
                .with(csrf())
                .contentType(APPLICATION_JSON)
                .content(updatedBookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.author").value("Updated Author"))
                .andExpect(jsonPath("$.price").value(35));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testUpdateBook_NotFound() throws Exception {
        String updatedBookJson = "{ \"title\": \"Updated Title\", \"author\": \"Updated Author\", \"publicationYear\": 2022, \"isbn\": \"0987654321\", \"price\": 35 }";

        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/books/999")
                .with(csrf())
                .contentType(APPLICATION_JSON)
                .content(updatedBookJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testDeleteBook() throws Exception {
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);

        mockMvc.perform(delete("/api/books/1")
                .with(csrf()))
                .andExpect(status().isOk());

        verify(bookRepository).deleteById(1L);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testDeleteBook_NotFound() throws Exception {
        doNothing().when(bookRepository).deleteById(anyLong());

        mockMvc.perform(delete("/api/books/999")
                .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/books")
                .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testAccessWithUserRole() throws Exception {
        mockMvc.perform(get("/api/books")
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testAccessWithAdminRole() throws Exception {
        mockMvc.perform(get("/api/books")
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testAddBook_MissingFields() throws Exception {
        String invalidBookJson = "{ \"title\": \"\", \"author\": \"\", \"publicationYear\": null, \"isbn\": \"\", \"price\": null }";

        mockMvc.perform(post("/api/books")
                .with(csrf())
                .contentType(APPLICATION_JSON)
                .content(invalidBookJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testExceptionHandling() throws Exception {
        when(bookRepository.findById(1L)).thenThrow(new RuntimeException("Test Exception"));

        mockMvc.perform(get("/api/books/1")
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("An unexpected error occurred."));
    }
}
