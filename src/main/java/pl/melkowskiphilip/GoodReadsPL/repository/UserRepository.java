package pl.melkowskiphilip.GoodReadsPL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.melkowskiphilip.GoodReadsPL.entity.Role;
import pl.melkowskiphilip.GoodReadsPL.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<User> findAllByRole(Role role);

    Optional<User> findByUsernameIgnoreCase(String username);

    // srednia liczba recenzji uzytkownika
    @Query("SELECT AVG(size(u.reviews)) FROM User u")
    Double findAverageReviewCount();

    // liczba recenzji danego uzytkownika
    @Query("SELECT size(u.reviews) FROM User u WHERE u.id = :id")
    Integer findReviewCount(Long id);


}
