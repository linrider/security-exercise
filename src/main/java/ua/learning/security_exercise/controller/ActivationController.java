package ua.learning.security_exercise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;
import ua.learning.security_exercise.service.ActivationServise;

@Controller
@RequiredArgsConstructor
public class ActivationController {
    public final ActivationServise activationServise;

    @GetMapping("/activation/{code}")
    public String activateCustomer(@PathVariable("code") String code) {
        if (activationServise.activateAccount(code)) {
            return "auth/succes_activation";
        } else {
            return "auth/fail_activation";
        }
    }
}
