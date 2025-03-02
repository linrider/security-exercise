package ua.learning.security_exercise.service;

import java.util.List;
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

    public Optional<Customer> findByUsername(String username) {
        return jdbcTemplate.query("select * from customer where username=?", 
                new BeanPropertyRowMapper<>(Customer.class), username)
                .stream()
                .findAny();
    }

    public List<Customer> getAll() {
        return jdbcTemplate.query("SELECT * FROM customer", new BeanPropertyRowMapper<>(Customer.class));
    }
    
    public List<Customer> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM customer WHERE role='ROLE_USER'", new BeanPropertyRowMapper<>(Customer.class));
    }
    
    public List<Customer> getAllAdmins() {
        return jdbcTemplate.query("SELECT * FROM customer WHERE role='ROLE_ADMIN'", new BeanPropertyRowMapper<>(Customer.class));
    }

    public void setAdminRole(int id) {
        jdbcTemplate.update("UPDATE customer SET role='ROLE_ADMIN' WHERE id=?", id);
    }

    public void returnUserRole(int id) {
        jdbcTemplate.update("UPDATE customer SET role='ROLE_USER' WHERE id=?", id);
    }

    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM customer WHERE id=?", id);
    }
}
