    package pl.melkowskiphilip.GoodReadsPL.repository;

    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.stereotype.Repository;
    import pl.melkowskiphilip.GoodReadsPL.entity.Book;
    import pl.melkowskiphilip.GoodReadsPL.entity.Genre;

    import java.util.List;

    @Repository
    public interface BookRepository extends JpaRepository<Book, Long> {

        //   średnia ocen
        @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
        Double findAverageRatingForBook(Long bookId);
    }

