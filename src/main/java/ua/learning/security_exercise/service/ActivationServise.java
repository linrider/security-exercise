package ua.learning.security_exercise.service;

import java.util.Optional;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.learning.security_exercise.model.Customer;

@Service
@RequiredArgsConstructor
public class ActivationServise {
    private final JdbcTemplate jdbcTemplate;

 public String saveActivation(String email) {
        String code = generateCode();
        jdbcTemplate.update("INSERT INTO activation (customer_email, key) VALUES (?, ?)", email, code);
        return code;
    } 

    public boolean activateAccount(String key) {
        Optional<Customer> customer = jdbcTemplate.query("SELECT customer.* FROM customer JOIN activation ON " + 
        "customer.email = activation.customer_email WHERE activation.key=?", new BeanPropertyRowMapper<>(Customer.class), key)
        .stream()
        .findAny();
        if (customer.isPresent()) {
            jdbcTemplate.update("UPDATE customer SET enable=true WHERE customer.id=?", customer.get().getId());
            jdbcTemplate.update("DELETE FROM activation WHERE key=?", key);
            return true;
        }
        return false;
    }

    private String generateCode() {
        StringBuilder builder = new StringBuilder();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 14; i++) {
            builder.append(alphabet.charAt((int) (Math.random() * alphabet.length())));
        }
        return builder.toString();
    }    
}
