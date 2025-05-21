package com.example.blogServer.security;

import com.example.blogServer.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Przydzielamy domyślną rolę ROLE_USER wszystkim użytkownikom
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
        // Używamy pola enabled z encji User
        return user.isEnabled();
    }

    // Metoda pomocnicza do dostępu do oryginalnego obiektu User
    public User getUser() {
        return user;
    }

    // Możemy dodać pomocnicze metody, aby uzyskać dostęp do
    // dodatkowych pól z encji User, np. do pola name
    public String getName() {
        return user.getName();
    }
}
