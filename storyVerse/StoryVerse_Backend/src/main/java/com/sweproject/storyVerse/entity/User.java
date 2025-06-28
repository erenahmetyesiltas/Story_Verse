package com.sweproject.storyVerse.entity;

import com.sweproject.storyVerse.enums.Gender;
import com.sweproject.storyVerse.security.user.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseEntity implements UserDetails {

    @Column(unique = true)
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    private String description; // A description in his/her personal page

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    @ManyToMany
    @JoinTable(
            name="user_genre",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
