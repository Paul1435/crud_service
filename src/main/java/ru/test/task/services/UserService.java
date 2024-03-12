package ru.test.task.services;


import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.test.task.dto.ImpUserDtoMapper;
import ru.test.task.dto.GetUserDTO;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import ru.test.task.dto.PutUserDTO;
import ru.test.task.models.Interest;
import ru.test.task.models.User;
import ru.test.task.repositories.InterestRepository;
import ru.test.task.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final InterestRepository interestRepository;
    private final ImpUserDtoMapper userDTOMapper;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUserNameContaining(username).stream().findFirst();
    }

    public List<GetUserDTO> getAllUsers() {
        return new ArrayList<>(
                userRepository
                        .findAll()
                        .stream()
                        .map(userDTOMapper)
                        .toList());
    }

    public Optional<User> getUserByFullname(String fullname) {
        return userRepository.findUserByFullName(fullname);
    }


    public GetUserDTO getUserById(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return userDTOMapper.apply(user.get());
        }
        throw new ResourceNotFoundException("User not found");
    }


    @Transactional
    public void saveOnlyUser(User user) {
        List<Interest> userInterests = user.getInterests();
        List<Interest> existingInterests = new ArrayList<>();
        if (userInterests != null) {
            for (Interest interest : userInterests) {
                Interest existingInterest = interestRepository.findInterestByLabel(interest.getLabel());
                if (existingInterest != null) {
                    existingInterests.add(existingInterest);
                } else {
                    throw new RuntimeException("Interest with label '" + interest.getLabel() + "' does not exist in the database.");
                }
            }
        }
        user.setInterests(existingInterests);
        userRepository.save(user);
    }


    @Transactional
    public GetUserDTO update(int id, PutUserDTO user) {
        User userToChange = userRepository.findById(id).orElseThrow(() -> new ResourceAccessException("Not found user"));
        if (user.getInterests() != null) {
            Set<String> labels = user.getInterests().stream().map(Interest::getLabel).collect(Collectors.toSet());
            List<Interest> interests = new ArrayList<>();
            for (String label : labels) {
                Interest interestFromDb = interestRepository.findInterestByLabel(label);
                if (interestFromDb != null) {
                    interests.add(interestFromDb);
                } else {
                    throw new ResourceAccessException(label + " not found");
                }
            }
            userToChange.setInterests(interests);
        }
        if (user.getUserName() != null) {
            userToChange.setUserName(user.getUserName());
        }
        if (user.getEmail() != null) {
            userToChange.setEmail(user.getEmail());
        }
        if (user.getFullName() != null) {
            userToChange.setFullName(user.getFullName());
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            userToChange.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(userToChange);
        return userDTOMapper.apply(userToChange);
    }


    public static void removeInterest(User user, int interestId) {
        Interest interest = user.getInterests().stream()
                .filter(t -> t.getId() == interestId)
                .findFirst()
                .orElse(null);
        if (interest != null) {
            user.getInterests().remove(interest);
            interest.getUsers().remove(user);
        }
    }

    @Transactional
    public void delete(int id) {
        userRepository.deleteById(id);
    }
}
