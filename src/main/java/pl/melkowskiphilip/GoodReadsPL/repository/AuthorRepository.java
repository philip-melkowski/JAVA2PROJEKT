package pl.melkowskiphilip.GoodReadsPL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.melkowskiphilip.GoodReadsPL.entity.Author;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    // autorzy ktorzy maja fragment w imieniu lub nazwisku
    @Query("SELECT a FROM Author a where LOWER(a.name) LIKE LOWER(CONCAT('%', :part, '%'))" +
            "OR LOWER(a.surname) LIKE LOWER(CONCAT('%', :part, '%'))")
    List<Author> findByNameOrSurnameContainingIgnoreCase(String part);

    // czy istnieje autor - zeby nie dodawac duplikatu
    boolean existsByNameAndSurname(String name, String surname);


}
