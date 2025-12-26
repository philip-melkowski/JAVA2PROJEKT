package pl.melkowskiphilip.GoodReadsPL.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.melkowskiphilip.GoodReadsPL.entity.Book;
import pl.melkowskiphilip.GoodReadsPL.entity.Genre;

import jakarta.persistence.criteria.Predicate;


// cb.conjunction() - to jest odpowiednik true w SQL
// TUTAJ TYLKO METODY FILTRUJĄCE - SORTOWANIE BĘDZIE WYŻEJ
public class BookSpecification {

    // filtr po gatunku książki
    public static Specification<Book> hasGenre(Genre genre) {
        return (root, query, cb) -> {
            if (genre == null) {
                return cb.conjunction(); // brak warunku
            }
            return cb.equal(root.get("genre"), genre);
        };
    }

    // filtr po autorze (ID autora)
    public static Specification<Book> hasAuthor(Long authorId) {
        return (root, query, cb) -> {
            if (authorId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("author").get("id"), authorId);
        };
    }

    // filtr po fragmencie tytułu (case-insensitive)
    public static Specification<Book> titleContains(String title) {
        return (root, query, cb) -> {
            if (title == null || title.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(
                    cb.lower(root.get("title")),
                    "%" + title.toLowerCase() + "%"
            );
        };
    }
}
