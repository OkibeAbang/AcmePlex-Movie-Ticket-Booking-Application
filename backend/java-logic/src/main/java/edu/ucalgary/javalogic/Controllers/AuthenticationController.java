package edu.ucalgary.javalogic.Controllers;

import edu.ucalgary.javalogic.Entities.User;
import edu.ucalgary.javalogic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/authentication")
@CrossOrigin(origins = "*")
public class AuthenticationController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody User user) {
        // Check if the email already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
//            return ResponseEntity.badRequest().body("Email already exists.");
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Email already exists.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Set default membership if not specified
        if (user.getPremium() == null) {
            user.setPremium(false); // Default to regular membership
        }

        user.setCredit(0.00);

        // Save user to database without encoding the password
        userRepository.save(user);

        String membership = user.getPremium() ? "Premium" : "Regular";
//        return ResponseEntity.ok("Signup successful! Membership: " + membership);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Signup Successful! Membership: " + membership);
        response.put("user_id", user.getId());

        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User loginUser) {
        System.out.println("Received Login Request: " + loginUser);
        // Find user by email
        User existingUser = userRepository.findByEmail(loginUser.getEmail());

        // Validate credentials (compare plain password)
        if (existingUser == null || !existingUser.getPassword().equals(loginUser.getPassword())) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid email or password.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Return login success with membership type
        Map<String, Object> response = new HashMap<>();
        response.put("userId", existingUser.getId());
        response.put("isAdmin", false);
        response.put("isPaid", existingUser.getPremium());

        return ResponseEntity.ok(response);
    }



}
