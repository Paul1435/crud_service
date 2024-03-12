package ru.test.task.services;

import lombok.AllArgsConstructor;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import ru.test.task.exception.BadRequestException;
import ru.test.task.models.Interest;
import ru.test.task.models.User;
import ru.test.task.repositories.InterestRepository;
import ru.test.task.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class InterestService {
    private final UserRepository userRepository;
    private final InterestRepository interestRepository;


    public Interest findById(int id) {
        return interestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("interest with id = " + id + " not found"));
    }

    public List<User> findAllUserById(int id) {
        List<User> users = userRepository.findUserByInterestsId(id);
        if (users == null || users.isEmpty()) {
            throw new ResourceNotFoundException("users for interest with id = " + id + " not found");
        }
        return users;
    }

    @Transactional
    public Interest updateInterest(int id, Interest toUpdate) {
        var oldInterest = interestRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("interest not found"));
        oldInterest.setUsers(toUpdate.getUsers());
        oldInterest.setLabel(toUpdate.getLabel().toLowerCase());
        interestRepository.save(oldInterest);
        return oldInterest;
    }

    @Transactional
    public List<Interest> addInterestForUser(int idUser, Interest interest) {
        Optional<User> userOptional = userRepository.findById(idUser);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Interest existingInterest = interestRepository.findInterestByLabel(interest.getLabel().toLowerCase());
            if (!user.getInterests().contains(existingInterest)) {
                if (existingInterest != null) {
                    user.getInterests().add(existingInterest);
                    userRepository.save(user);
                    return new ArrayList<>(user.getInterests());
                }
            }
            throw new ResourceNotFoundException("You Can't   add interest because it's not exist");
        }
        throw new ResourceNotFoundException("User not found");
    }


    @Transactional
    public void deleteInterestForUser(int userId, int interestId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceAccessException("Not found user with id = " + userId));
        Optional<Interest> interesting = user.getInterests()
                .stream().filter(interest -> interest.getId() == interestId).findFirst();
        if (interesting.isEmpty()) {
            throw new BadRequestException("Interest with " + interestId + "not found");
        }
        UserService.removeInterest(user, interestId);
    }

    @Transactional
    public Interest createNewInterest(Interest interest) {
        List<Interest> interests = findAllInterests();
        long count = interests.stream().filter((interestBD) ->
                interestBD.getLabel().equalsIgnoreCase(interest.getLabel())).count();
        interest.setLabel(interest.getLabel().toLowerCase());
        interestRepository.save(interest);
        return interest;
    }

    @Transactional
    public void deleteInterest(int interestId) {
        if (interestRepository.existsById(interestId)) {
            interestRepository.deleteById(interestId);
        }
        throw new ResourceNotFoundException(interestId + " interest not found");
    }

    public List<Interest> findAllInterests() {
        return interestRepository.findAll();
    }

    public List<Interest> findByUserId(int id) {
        if (!interestRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with " + id + " not Found");
        }
        List<Interest> interests = interestRepository.findInterestsByUsersId(id);
        if (interests == null || interests.isEmpty()) {
            throw new ResourceNotFoundException("No interests for user with id " + id);
        }
        return interests;
    }

    public Optional<Interest> findByLabel(String label) {
        return Optional.ofNullable(interestRepository.findInterestByLabel(label));
    }
}
