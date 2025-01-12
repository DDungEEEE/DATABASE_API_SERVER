package net.ddns.sbapiserver.security;

import lombok.Getter;

@Getter
public enum UserType {
    STAFF("ROLE_STAFF"),
    CLIENT("ROLE_CLIENT");

    private final String role;

    UserType(String role) {
        this.role = role;
    }

}
