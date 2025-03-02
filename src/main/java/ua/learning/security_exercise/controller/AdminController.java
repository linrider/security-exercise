package ua.learning.security_exercise.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import ua.learning.security_exercise.model.Customer;
import ua.learning.security_exercise.security.CustomerDetails;
import ua.learning.security_exercise.service.CustomerService;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final CustomerService customerService;

    @GetMapping()
    public String adminPage(@AuthenticationPrincipal CustomerDetails customerDetails,
            @AuthenticationPrincipal OAuth2User oauth2User, Model model) {
        if (customerDetails != null) {
            Customer customer = customerDetails.customer();
            if(customer.getRole().equals("ROLE_ROOTADMIN")) {
                model.addAttribute("rootadmin", customer);
            } else {
                model.addAttribute("admin", customer);
            }
        } else if (oauth2User != null) {
            String email = (String) oauth2User.getAttributes().get("email");
            Customer customer = customerService.findByEmail(email).orElseThrow();
            if(customer.getRole().equals("ROLE_ROOTADMIN")) {
                model.addAttribute("rootadmin", customer);
            } else {
                model.addAttribute("admin", customer);
            }
        }
        model.addAttribute("users", customerService.getAllUsers());
        model.addAttribute("admins", customerService.getAllAdmins());
        return "admin/administration";
    }

    @PutMapping("/make-admin")
    public String makeAdmin(@RequestParam("id") int id) {
        customerService.setAdminRole(id);
        return "redirect:/admin";
    }

    @PutMapping("/return-user")
    public String returnUser(@RequestParam("id") int id) {
        customerService.returnUserRole(id);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam("id") int id) {
        customerService.deleteById(id);
        return "redirect:/admin";
    }

}
