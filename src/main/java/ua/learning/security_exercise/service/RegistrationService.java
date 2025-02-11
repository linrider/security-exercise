package ua.learning.security_exercise.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.learning.security_exercise.model.Customer;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public void register(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRole("ROLE_USER");
        jdbcTemplate.update("INSERT INTO customer (username, password, email, role) " +
                "VALUES (?, ?, ?, ?)",
                customer.getUsername(), customer.getPassword(), customer.getEmail(), customer.getRole());
    }
}
