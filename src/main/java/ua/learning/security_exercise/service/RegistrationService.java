package ua.learning.security_exercise.service;

import java.util.Map;
import java.util.Optional;

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
    private final ActivationServise activationServise;
    private final EmailService emailService;
    private final CustomerService customerService;

    public void register(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRole("ROLE_USER");
        customer.setEnable(false);
        jdbcTemplate.update("INSERT INTO customer (username, password, email, role, enable) " +
                "VALUES (?, ?, ?, ?, ?)",
                customer.getUsername(), customer.getPassword(), customer.getEmail(), customer.getRole(),
                customer.isEnable());
        String key = activationServise.saveActivation(customer.getEmail());
        emailService.sendActivationEmail(customer.getEmail(), key);
    }

    public Customer findOrRegisterOauth2User(Map<String, Object> atributs) {
        String email = (String) atributs.get("email");
        Optional<Customer> customer = customerService.findByEmail(email);

        if (customer.isPresent()) {
            return customer.get();
        }

        Customer customer2 = new Customer();
        customer2.setEmail(email);
        customer2.setUsername(email);
        customer2.setRole("ROLE_USER");
        customer2.setEnable(true);
        customer2.setProvider("google");
        customer2.setProviderId((String) atributs.get("sub"));
        String password = generatePassword();
        customer2.setPassword(passwordEncoder.encode(password));

        emailService.sendWelcomeEmail(email, password);

        jdbcTemplate.update("INSERT INTO customer (username, password, email, role, enable, provider, provider_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)",
                customer2.getUsername(), customer2.getPassword(), customer2.getEmail(), customer2.getRole(),
                customer2.isEnable(), customer2.getProvider(), customer2.getProviderId());
        return customerService.findByEmail(email).get();
    }

    private String generatePassword() {
        StringBuilder builder = new StringBuilder();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789*/.%";
        for (int i = 0; i < 14; i++) {
            builder.append(alphabet.charAt((int) (Math.random() * alphabet.length())));
        }
        return builder.toString();
    }
}
