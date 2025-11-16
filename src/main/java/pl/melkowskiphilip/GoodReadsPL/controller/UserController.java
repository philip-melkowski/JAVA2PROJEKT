package pl.melkowskiphilip.GoodReadsPL.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.melkowskiphilip.GoodReadsPL.dto.UserDTO;
import pl.melkowskiphilip.GoodReadsPL.dto.UserRegisterDTO;
import pl.melkowskiphilip.GoodReadsPL.service.UserService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() { return ResponseEntity.ok(userService.findAll()); }

    @GetMapping("/findById/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) { return ResponseEntity.ok(userService.findById(id)); }

    @GetMapping("/findByUsername/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) { return ResponseEntity.ok(userService.findByUsernameIgnoreCase(username)); }

    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) { return ResponseEntity.ok(userService.findByEmail(email)); }


    // dodanie uzytkownika
    @PostMapping
    public ResponseEntity<UserDTO> saveUser(@Valid @RequestBody UserRegisterDTO user)
    {
        return ResponseEntity.ok(userService.save(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id)
    {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO updatedUser)
    {
        return ResponseEntity.ok(userService.updateUser(id, updatedUser));
    }

    @GetMapping("/avgRevCount")
    public ResponseEntity<Double> getAvgRevCount()
    {
        return ResponseEntity.ok(userService.findAverageReviewCount());
    }

    @GetMapping("revCount/{id}")
    public ResponseEntity<Integer> getRevCount(@PathVariable Long id)
    {
        return ResponseEntity.ok(userService.findReviewCount(id));
    }



}
