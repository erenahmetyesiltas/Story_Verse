package com.sweproject.storyVerse.security.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sweproject.storyVerse.security.user.Permission.*;

@RequiredArgsConstructor
public enum Role {
    ADMIN(
            Set.of(
                    ADMIN_CREATE,
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    AUTHOR_CREATE,
                    AUTHOR_READ,
                    AUTHOR_UPDATE,
                    AUTHOR_DELETE,
                    CONTRIBUTOR_CREATE,
                    CONTRIBUTOR_READ,
                    CONTRIBUTOR_UPDATE,
                    CONTRIBUTOR_DELETE
            )
    ),
    AUTHOR(
            Set.of(
                    AUTHOR_CREATE,
                    AUTHOR_READ,
                    AUTHOR_UPDATE,
                    AUTHOR_DELETE
            )
    ),
    CONTRIBUTOR(
            Set.of(
                    CONTRIBUTOR_CREATE,
                    CONTRIBUTOR_READ,
                    CONTRIBUTOR_UPDATE,
                    CONTRIBUTOR_DELETE
            )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}