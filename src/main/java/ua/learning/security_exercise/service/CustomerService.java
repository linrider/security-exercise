package ua.learning.security_exercise.service;

import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.learning.security_exercise.model.Customer;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final JdbcTemplate jdbcTemplate;

    public Customer getById(int id) {
        return jdbcTemplate
                .queryForObject(
                        "SELECT * FROM customer WHERE id=?",
                        new BeanPropertyRowMapper<>(Customer.class),
                        id);
    }

    public Optional<Customer> findByEmail(String email) {
        try {
        Customer customer = jdbcTemplate.queryForObject("select * from customer where email=?", 
                new BeanPropertyRowMapper<>(Customer.class),
                email);
                return Optional.ofNullable(customer);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @SuppressWarnings("deprecation")
    public Optional<Customer> findByUsername(String username) {
        return jdbcTemplate.query("select * from customer where username=?", new Object[] { username },
                new BeanPropertyRowMapper<>(Customer.class))
                .stream()
                .findAny();
    }
}
