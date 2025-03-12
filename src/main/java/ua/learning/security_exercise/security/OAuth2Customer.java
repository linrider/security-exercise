package ua.learning.security_exercise.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.AllArgsConstructor;
import ua.learning.security_exercise.model.Customer;

@AllArgsConstructor
public class OAuth2Customer implements OAuth2User{
    private Customer customer;

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of(
            "id", customer.getId(),
            "username", customer.getUsername(),
            "email", customer.getEmail(),
            "role", customer.getRole(),
            "enable", customer.isEnable(),
            "provider", customer.getProvider(),
            "providerId", customer.getProviderId() 
        );

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(customer.getRole()));
    }

    @Override
    public String getName() {
        return customer.getUsername();
    }

}
