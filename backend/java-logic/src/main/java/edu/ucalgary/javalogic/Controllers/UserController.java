package edu.ucalgary.javalogic.Controllers;

import edu.ucalgary.javalogic.repository.UserRepository;
import edu.ucalgary.javalogic.Entities.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    if (userDetails.getEmail() != null) user.setEmail(userDetails.getEmail());
                    if (userDetails.getPassword() != null) user.setPassword(userDetails.getPassword());
                    if (userDetails.getFirstName() != null) user.setFirstName(userDetails.getFirstName());
                    if (userDetails.getLastName() != null) user.setLastName(userDetails.getLastName());
                    if (userDetails.getPremium() != null) user.setPremium(userDetails.getPremium());
                    if (userDetails.getPremiumSignupDate() != null)
                        user.setPremiumSignupDate(userDetails.getPremiumSignupDate());
                    if (userDetails.getCredit() != null) user.setCredit(userDetails.getCredit());
                    return ResponseEntity.ok(userRepository.save(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/checkMembership")
    public ResponseEntity<String> checkMembership(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        User user = userRepository.findById(id).get();

        if (!user.getPremium()) {
            return ResponseEntity.ok("User is not premium");
        }

        Date currentDate = new Date();

        // Use Calendar to add a year to the input date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(user.getPremiumSignupDate());
        calendar.add(Calendar.YEAR, 1);

        // Check if the adjusted date is before the current date
        if (!calendar.getTime().after(currentDate)) {
            user.setPremium(false);
            return ResponseEntity.ok("User is no longer premium");
        } else {
            return ResponseEntity.ok("User is still premium");
        }

    }


}



