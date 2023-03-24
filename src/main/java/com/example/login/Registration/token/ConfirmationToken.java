package com.example.login.Registration.token;

import com.example.login.AppUser.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {

    @Id
    @SequenceGenerator(
            name = "confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1
    )
    // alt+j to select all
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "confirmation_token_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiredAt;
    private LocalDateTime confirmedAt;

    //one user can have many tokens
    @ManyToOne
    @JoinColumn(nullable = false, name = "app_user_id")
    private AppUser appUser;

    public ConfirmationToken(
            String token,
            LocalDateTime createdAt,
            LocalDateTime expiresAt,
            AppUser appUser
    ) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expiresAt;
        this.appUser = appUser;
    }
}
