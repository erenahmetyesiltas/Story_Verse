package com.sweproject.storyVerse.security.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_CREATE("admin:create"),
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),
    AUTHOR_CREATE("author:create"),
    AUTHOR_READ("author:read"),
    AUTHOR_UPDATE("author:update"),
    AUTHOR_DELETE("author:delete"),
    CONTRIBUTOR_CREATE("contributor:create"),
    CONTRIBUTOR_READ("contributor:read"),
    CONTRIBUTOR_UPDATE("contributor:update"),
    CONTRIBUTOR_DELETE("contributor:delete");

    @Getter
    private final String permission;
}
