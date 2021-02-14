package ru.otus.spring.library.docker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.otus.spring.library.docker.models.*;
import ru.otus.spring.library.docker.repositories.*;
import ru.otus.spring.library.docker.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class PagesController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final BooksRepository booksRepository;
    private final BookCommentsRepository bookCommentsRepository;
    private final LookupsRepository lookupsRepository;
    private final AuthorsReporitory authorsReporitory;
    private final PublishingHousesRepository publishingHousesRepository;

    @Autowired
    public PagesController(UserService userService,
                           BooksRepository booksRepository,
                           UserRepository userRepository,
                           BookCommentsRepository bookCommentsRepository,
                           LookupsRepository lookupsRepository,
                           AuthorsReporitory authorsReporitory,
                           PublishingHousesRepository publishingHousesRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.booksRepository = booksRepository;
        this.bookCommentsRepository = bookCommentsRepository;
        this.lookupsRepository = lookupsRepository;
        this.authorsReporitory = authorsReporitory;
        this.publishingHousesRepository = publishingHousesRepository;
    }

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/users")
    public String usersList(Model model) {

        // Доступ только для администратора
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    // @GetMapping("/user/{userName}")
    @GetMapping("/user")
    public String user(@RequestParam(value="name")String userName, Model model) {
        // public String user(@PathVariable(value = "userName") String userName, Model model) {
        // @RequestParam(value="name")

        User user = userService.loadUserByUsername(userName);
        model.addAttribute("users", user);
        return "user";
    }

    @GetMapping("/books")
    public String bookList(Model model) {
        List<Book> books = booksRepository.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/book")
    public String newBook(Model model) {
        Book book = new Book();
        book.setAuthor(new Author());
        LookupValue lv = new LookupValue();
        lv.setKey(new LookupValueId());
        book.setGenre(lv);

        book.setPublishingHouse(new PublishingHouse());
        List<LookupValue> lookupValues =
                lookupsRepository.findByLookupTypeLanguage(LookupValue.GENRES_LOOKUP_TYPE, LookupValue.RU);

        List<Author> authors = authorsReporitory.findAll();
        List<PublishingHouse> publishingHouses = publishingHousesRepository.findAll();

        model.addAttribute("book", book);
        model.addAttribute("genres", lookupValues);
        model.addAttribute("authors", authors);
        model.addAttribute("pubhouses", publishingHouses);
        return "book";
    }

    @GetMapping("/books/edit/{id}")
    public String editBook(@PathVariable(value = "id") long bookId, Model model) {
        Optional<Book> optBook = booksRepository.findById(bookId);
        List<LookupValue> lookupValues =
                lookupsRepository.findByLookupTypeLanguage(LookupValue.GENRES_LOOKUP_TYPE, LookupValue.RU);

        List<Author> authors = authorsReporitory.findAll();
        List<PublishingHouse> publishingHouses = publishingHousesRepository.findAll();

        model.addAttribute("book", optBook.get());
        model.addAttribute("genres", lookupValues);
        model.addAttribute("authors", authors);
        model.addAttribute("pubhouses", publishingHouses);
        return "book";
    }

    @RequestMapping(value = "/books/save", method = RequestMethod.POST)
    public String saveBook(
            @RequestParam(value = "action", required = true) String action,
            HttpServletRequest request
    ) {
        if (action.equals("Cancel")) return "redirect:/books";

        Book book;
        String bookIdStr = request.getParameter("bookId");
        long bookId = (bookIdStr.isBlank() || bookIdStr.isEmpty()) ? -1 : Long.parseLong(bookIdStr);

        if (bookId == -1 || bookId == 0) {
            // новая книга
            book = new Book();
        } else {
            // изменение книги
            book = booksRepository.findById(bookId).get();
        }

        book.setName(request.getParameter("name"));
        Author author = authorsReporitory.findById(Long.parseLong(request.getParameter("author"))).get();
        book.setAuthor(author);
        book.setPages(Integer.parseInt(request.getParameter("pages")));
        String lookupCode = request.getParameter("genre");
        LookupValue genre =
                lookupsRepository.findByLookupTypeLookupCodeLanguage(
                        LookupValue.GENRES_LOOKUP_TYPE, lookupCode, LookupValue.RU
                );
        book.setGenre(genre);
        PublishingHouse pubhouse =
                publishingHousesRepository.findById(
                        Long.parseLong(request.getParameter("pubhouse"))
                ).get();
        book.setPublishingHouse(pubhouse);
        book.setPublishingYear(Integer.parseInt(request.getParameter("publishingYear")));
        book.setAgeLimit(request.getParameter("age_limit"));

        booksRepository.saveBookWithAcl(book);

        return "redirect:/books";
    }

    @Transactional
    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable(value = "id") long bookId, Model model) {
        bookCommentsRepository.deleteByBookId(bookId);
        booksRepository.deleteById(bookId);
        return "redirect:/books";
    }

    @GetMapping("/books/{id}/comments")
    public String bookComments(@PathVariable(value = "id") long bookId, Model model) {
        List<Comment> comments = bookCommentsRepository.findByBookId(bookId);
        model.addAttribute("comments", comments);
        model.addAttribute("bookId", bookId);
        return "comments";
    }

    @GetMapping("/books/{bookId}/comment/{commentId}/edit")
    @PreAuthorize("isCommentOwner(#commentId) or hasRole('ROLE_ADMIN')")
    public String editComment(
            @PathVariable(value = "bookId") long bookId,
            @PathVariable(value = "commentId") long commentId,
            Model model
    ) {
        Optional<Comment> optComment = bookCommentsRepository.findById(commentId);
        if (optComment.isEmpty()) return "under_construction"; // TODO: переделать на error page

        model.addAttribute("action", "Edit");
        model.addAttribute("bookId", bookId);
        model.addAttribute("comments", Collections.singletonList(optComment.get()));
        return "comment";
    }

    @GetMapping("/books/{bookId}/comment/new")
    public String newComment(
            @PathVariable(value = "bookId") long bookId,
            Model model
    ) {
        Comment comment = new Comment();
        comment.setId(-1);
        model.addAttribute("action", "Edit");
        model.addAttribute("bookId", bookId);
        model.addAttribute("comments", Collections.singletonList(comment));
        return "comment";
    }

    @GetMapping("/books/{bookId}/comment/{commentId}/delete")
    @PreAuthorize("isCommentOwner(#commentId) or hasRole('ROLE_ADMIN')")
    public String deleteComment(
            @PathVariable(value = "bookId") long bookId,
            @PathVariable(value = "commentId") long commentId,
            Model model
    ) {
        bookCommentsRepository.deleteById(commentId);

        URI uri = UriComponentsBuilder
                .fromUriString("/books/{bookId}/comments")
                .buildAndExpand(bookId)
                .encode()
                .toUri();

        return "redirect:" + uri.toString();
    }

    @RequestMapping(value = "/books/{bookId}/comment/{commentId}/save", method = RequestMethod.POST)
    @PreAuthorize("isCommentOwner(#commentId) or hasRole('ROLE_ADMIN')")
    public String saveComment(
            @PathVariable(value = "bookId") long bookId,
            @PathVariable(value = "commentId") long commentId,
            @RequestParam(value = "action", required = true) String action,
            HttpServletRequest request
    ) {
        if (action.equals("Submit")) {
            String commentText = request.getParameter("commentText");
            if (commentId == -1 || commentId == 0) {
                // новый комментарий
                bookCommentsRepository.addBookComment(bookId, commentText);
            } else {
                // изменение комментария
                bookCommentsRepository.updateBookComment(commentId, commentText);
            }
        }

        URI uri = UriComponentsBuilder
                .fromUriString("/books/{bookId}/comments")
                .buildAndExpand(bookId)
                .encode()
                .toUri();

        return "redirect:" + uri.toString();
    }
}
