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

    public static class PreAuthorizeRole{
        public static final String STAFF = "T(net.ddns.sbapiserver.security.UserType).STAFF.role)";
        public static final String CLIENT = "T(net.ddns.sbapiserver.security.UserType).CLIENT.role)";

    }
}
