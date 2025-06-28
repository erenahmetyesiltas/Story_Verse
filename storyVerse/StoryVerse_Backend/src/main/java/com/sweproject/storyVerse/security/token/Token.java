package com.sweproject.storyVerse.security.token;

import jakarta.persistence.*;
import lombok.*;
import com.sweproject.storyVerse.entity.User;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "token")
public class Token {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    public Long id;

    @Column(unique = true)
    public String token;

    public boolean expired;
    public boolean revoked;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
}
