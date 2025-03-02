package ua.learning.security_exercise.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import ua.learning.security_exercise.model.Customer;
import ua.learning.security_exercise.service.CustomerService;

@Component
@RequiredArgsConstructor
public class CustomerValidator implements Validator {
    private final CustomerService customerService;

    @SuppressWarnings("null")
    @Override
    public boolean supports(Class<?> clazz) {
        return Customer.class.equals(clazz);
    }

    @SuppressWarnings("null")
    @Override
    public void validate(Object target, Errors errors) {
        Customer customer = (Customer) target;

        if (customerService.findByEmail(customer.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "This email is already used");
        }

        if (customerService.findByUsername(customer.getUsername()).isPresent()) {
            errors.rejectValue("username", "", "This username is already used");
        }
    }

}
