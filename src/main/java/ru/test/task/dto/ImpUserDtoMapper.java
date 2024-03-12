package ru.test.task.dto;

import org.springframework.stereotype.Component;
import ru.test.task.models.User;

import java.util.function.Function;

@Component
public class ImpUserDtoMapper implements Function<User, GetUserDTO> {


    @Override
    public GetUserDTO apply(User user) {
        return new GetUserDTO(
                user.getId(),
                user.getFullName(),
                user.getUserName(),
                user.getEmail(),
                user.getInterests()
        );
    }
}
