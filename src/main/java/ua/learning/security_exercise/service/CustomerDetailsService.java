package ua.learning.security_exercise.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.learning.security_exercise.model.Customer;
import ua.learning.security_exercise.security.CustomerDetails;

@Service
@RequiredArgsConstructor
public class CustomerDetailsService implements UserDetailsService{
    private final CustomerService customerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> customer = customerService.findByUsername(username);
        if (customer.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User with %s username not found", username));
        }

        return new CustomerDetails(customer.get());
    }


}
