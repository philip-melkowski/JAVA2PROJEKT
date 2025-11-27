package pl.melkowskiphilip.GoodReadsPL.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="activation_tokens")
public class ActivationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


    // 7 dni na aktywacje konta, potem token nieważny.
    // docelowo po 7 dniach, token bylby usuwany i użytkownik by musial wygenerować nowy
    // ale nie wiem, czy to zaimplenetuje. na razie po prostu bedzie zmienna expiresAt
    @Column(nullable = false)
    private LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
