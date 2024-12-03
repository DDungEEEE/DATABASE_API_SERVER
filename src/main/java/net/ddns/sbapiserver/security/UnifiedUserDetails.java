package net.ddns.sbapiserver.security;

import lombok.Getter;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class UnifiedUserDetails implements UserDetails {
    private String userName;
    private String userPwd;
    private UserType userType;
    private Collection<? extends GrantedAuthority> authorities;

    // Clients UserDetails 반환
    public UnifiedUserDetails(Clients clients){
        this.userName = clients.getClientName();
        this.userPwd = clients.getClientPassword();
        this.userType = UserType.CLIENT;
        this.authorities = Collections.singleton(new SimpleGrantedAuthority(userType.getRole()));
    }

    // Staffs UserDetails 반환
    public UnifiedUserDetails(Staffs staffs){
        this.userName = staffs.getStaffUserId();
        this.userPwd = staffs.getStaffPassword();
        this.userType = UserType.STAFF;
        this.authorities = Collections.singleton(new SimpleGrantedAuthority(userType.getRole()));
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return userPwd;
    }

    @Override
    public String getUsername() {
        return userName;
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
