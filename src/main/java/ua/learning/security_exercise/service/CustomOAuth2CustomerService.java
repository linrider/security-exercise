package ua.learning.security_exercise.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.learning.security_exercise.model.Customer;
import ua.learning.security_exercise.security.OAuth2Customer;

@Service
@RequiredArgsConstructor
public class CustomOAuth2CustomerService extends DefaultOAuth2UserService {
    private final RegistrationService registrationService;
    private final CustomerService customerService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Customer customer = registrationService.findOrRegisterOauth2User(oAuth2User.getAttributes());
        // UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(oAuth2User,
        //         null, oAuth2User.getAuthorities());
        return new DefaultOAuth2User(Collections.singletonList(new SimpleGrantedAuthority(customer.getRole())),
                oAuth2User.getAttributes(), "email");

    }

    public OAuth2User loadRemember(String email) {
        Customer customer = customerService.findByEmail(email).get();
        OAuth2Customer oAuth2Customer = new OAuth2Customer(customer);
        return new DefaultOAuth2User(Collections.singletonList(
            new SimpleGrantedAuthority(customer.getRole())), 
            oAuth2Customer.getAttributes(), 
            "email");
    }

}
