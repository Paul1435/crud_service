package ru.test.task.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.test.task.models.Interest;
import ru.test.task.services.InterestService;
import ru.test.task.util.InterestValidate;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class InterestController {

    private final InterestService interestService;
    private final InterestValidate interestValidate;

    @GetMapping("/interests")
    public List<Interest> getAllInterests() {
        return interestService.findAllInterests();
    }

    @GetMapping("/users/{id}/interests")
    public ResponseEntity<List<Interest>> getAllInterests(@PathVariable(name = "id") int userId) {
        return ResponseEntity.ok(interestService.findByUserId(userId));
    }

    @GetMapping("/interests/{id}")
    public ResponseEntity<?> getInterestsById(@PathVariable(value = "id") int interestId) {
        return ResponseEntity.ok(interestService.findById(interestId));
    }

    @GetMapping("/interests/{id}/users")
    public ResponseEntity<?> getAllUsersByInterestId(@PathVariable(value = "id") int id) {
        return ResponseEntity.ok(interestService.findAllUserById(id));
    }

    @PostMapping("/users/{userId}/interests")
    public ResponseEntity<?> addInterest(@PathVariable(name = "userId") int userId, @RequestBody @Valid Interest interest) {
        List<Interest> interests = interestService.addInterestForUser(userId, interest);
        return ResponseEntity.ok(interests);
    }

    @PutMapping("/interests/{id}")
    public ResponseEntity<?> updateInterest(@PathVariable(name = "id") int id, @RequestBody @Valid Interest updateInterest,
                                            BindingResult bindingResult) {
        updateInterest.setId(id);
        interestValidate.validate(updateInterest, bindingResult);
        interestService.updateInterest(id, updateInterest);
        return ResponseEntity.ok(updateInterest);
    }

    @DeleteMapping("users/{user_id}/interests/{interest_id}")
    public ResponseEntity<HttpStatus> deleteInterestForUser(@PathVariable("user_id") int userId,
                                                            @PathVariable("interest_id") int interestId) {
        interestService.deleteInterestForUser(userId, interestId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }

    @DeleteMapping("interests/{interest_id}")
    public RedirectView deleteInterest(@PathVariable("interest_id") int interestId) {
        interestService.deleteInterest(interestId);
        return new RedirectView("/api" + "/interests", true);
    }

    @PostMapping("interests")
    public ResponseEntity<?> createInterest(@RequestBody @Valid Interest newInterest, BindingResult bindingResult) {
        interestValidate.validate(newInterest, bindingResult);
        Interest interest = interestService.createNewInterest(newInterest);
        return ResponseEntity.ok(interest);
    }
}
