package ua.learning.security_exercise.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import ua.learning.security_exercise.model.Customer;
import ua.learning.security_exercise.security.CustomerDetails;
import ua.learning.security_exercise.service.CustomerService;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final CustomerService customerService;

    @GetMapping
    public String getMainPage(@AuthenticationPrincipal CustomerDetails customerDetails,
            @AuthenticationPrincipal OAuth2User oauth2User, Model model) {
        if (customerDetails != null) {
            Customer customer = customerDetails.customer();
            model.addAttribute("customer", customer);
        } else if (oauth2User != null) {
            String email = (String) oauth2User.getAttributes().get("email");
            Customer customer = customerService.findByEmail(email).orElseThrow();
            model.addAttribute("customer", customer);
        }
        return "main";
    }
}
