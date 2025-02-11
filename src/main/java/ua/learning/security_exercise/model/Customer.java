package ua.learning.security_exercise.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private int id;

    @NotBlank(message = "The username couldn't be empty")
    @Size(min = 3, max = 30, message = "The username length should be from 3 to 30")
    private String username;

    @NotBlank(message = "The email couldn't be empty")
    @Email(message = "Email should look like example@example.com")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 3, max = 30, message = "Password must be between 3 and 30 characters")
    private String password;

    private String role;
}
