package ua.learning.security_exercise.security;

import java.io.IOException;
import java.util.Base64;
import jakarta.servlet.http.Cookie;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2CookieFilter implements AuthenticationSuccessHandler {
    private static final String COOKIE_NAME = "OAUTH2_REMEMBER_ME";
    private static final int COOKIE_EXPIRE = 7*24*60*60;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        if (authentication.getPrincipal() instanceof OAuth2User) {
            String tokenValue = authentication.getName();
            String encodedToken = Base64.getEncoder().encodeToString(tokenValue.getBytes());
            Cookie cookie = new Cookie(COOKIE_NAME, encodedToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setMaxAge(COOKIE_EXPIRE);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        response.sendRedirect("/");
    }

}
