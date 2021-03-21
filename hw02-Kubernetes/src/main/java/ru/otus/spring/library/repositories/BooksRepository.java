package ru.otus.spring.library.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.library.models.Book;

import java.util.List;

@RepositoryRestResource(path="books")
public interface BooksRepository extends JpaRepository<Book, Long>, BookRepositoryCustom {

    // https://www.javacodemonk.com/what-is-n-1-problem-in-hibernate-how-will-you-identify-and-solve-it-894097b9
    // Вариант избежать N+1 problem
    @EntityGraph(attributePaths = {"author", "publishingHouse", "genre"})
    @PostFilter("hasPermission(filterObject, 'READ')")
    // @RestResource(path="allbooks", rel="allbooks")
    List<Book> findAll();

    // @RestResource(path="search", rel="search")
    // Другой вариант использовать LEFT JOIN FETCH
    @Query("SELECT b FROM Book b \n" +
            "LEFT JOIN FETCH b.author \n" +
            "LEFT JOIN FETCH b.publishingHouse \n" +
            "LEFT JOIN FETCH b.genre \n" +
            "WHERE (:bookName IS NULL OR :bookName IS '' OR UPPER(b.name) LIKE UPPER(:bookName)) \n" +
            "AND (:genreMeaning IS NULL OR :genreMeaning IS '' OR UPPER(b.genre.meaning) LIKE UPPER(:genreMeaning)) \n" +
            "AND (:authorName IS NULL OR :authorName IS '' OR UPPER(b.author.name) LIKE UPPER(:authorName)) \n" +
            "AND (:publishingHouseName IS NULL OR :publishingHouseName IS '' OR UPPER(b.publishingHouse.name) LIKE UPPER(:publishingHouseName)) \n" +
            "AND (:publishingYearFrom = 0 OR :publishingYearFrom <= b.publishingYear) \n" +
            "AND (:publishingYearTo = 0 OR b.publishingYear <= :publishingYearTo) \n" +
            "AND (:pagesFrom = 0 OR :pagesFrom <= b.pages) \n" +
            "AND (:pagesTo = 0 OR b.pages <= :pagesTo) \n"
    )
    List<Book> getBooks(
            @Param("bookName") String bookName,
            @Param("genreMeaning") String genreMeaning,
            @Param("authorName") String authorName,
            @Param("publishingHouseName") String publishingHouseName,
            @Param("publishingYearFrom") int publishingYearFrom,
            @Param("publishingYearTo") int publishingYearTo,
            @Param("pagesFrom") int pagesFrom,
            @Param("pagesTo") int pagesTo
    );

    @Transactional
    void deleteById(long bookId);
}
