package pl.melkowskiphilip.GoodReadsPL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.melkowskiphilip.GoodReadsPL.entity.ActivationToken;
import pl.melkowskiphilip.GoodReadsPL.entity.User;

import java.util.Optional;

@Repository
public interface ActivationTokenRepository extends JpaRepository <ActivationToken, Long> {
    Optional<ActivationToken> findByToken(String token);
    Optional<ActivationToken> findByUser(User user);
}
