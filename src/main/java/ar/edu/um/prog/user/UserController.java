package ar.edu.um.prog.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getall")
    public ResponseEntity<ArrayList<User>> getAllUsers() {
        ArrayList<User> users = this.userService.getUser();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        Optional<User> user = this.userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(
            @RequestParam Integer userId,
            @RequestBody User user) {
        Optional<User> updatedUser = userService.updateUser(user, Long.valueOf(userId));
        if (updatedUser == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    

    @PostMapping("/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User savedUser = this.userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        boolean isDeleted = this.userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.ok("User with Id " + id + " has been deleted");
        } else {
            return ResponseEntity.status(500).body("Error, User with Id " + id + " has not been deleted");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User request, @PathVariable("id") Long id) {
        Optional<User> updatedUser = this.userService.updateUser(request, id);
        return updatedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getAuthenticatedUser() {
        Optional<User> user = userService.getAuthenticatedUser();
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(401).build());
    }
}
