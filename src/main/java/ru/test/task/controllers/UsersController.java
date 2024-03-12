package ru.test.task.controllers;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.test.task.dto.GetUserDTO;
import ru.test.task.dto.ImpUserDtoMapper;
import ru.test.task.dto.PutUserDTO;
import ru.test.task.models.User;
import ru.test.task.services.UserService;
import ru.test.task.util.UserValidator;

import java.net.URI;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UsersController {

    private final UserService userService;
    private final UserValidator userValidator;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<GetUserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserDTO> getUserById(@PathVariable("id") int id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

//    @PostMapping()
//    public ResponseEntity<?> createUser(@RequestBody @Valid PutUserDTO user, BindingResult bindingResult) {
//        userValidator.validate(user, bindingResult);
//        userService.saveOnlyUser(user);//todo:remake
//        return ResponseEntity
//                .created(URI.create("/api/users/" + user.getId()))
//                .body(user);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<GetUserDTO> updateUser(@PathVariable("id") int id, @RequestBody @Valid PutUserDTO user, BindingResult bindingResult) {
        user.setId(id);
        userValidator.validate(user, bindingResult);

        return ResponseEntity.ok(userService.update(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") int id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
