package ua.learning.security_exercise.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ua.learning.security_exercise.security.CustomerDetails;

@Controller
public class MainController {
    @GetMapping
    public String getMainPage(@AuthenticationPrincipal CustomerDetails customerDetails, Model model) {
        model.addAttribute("customer", customerDetails.customer());
        return "main"; 
    }
}
