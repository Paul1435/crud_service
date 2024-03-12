package ru.test.task.dto;

import lombok.Getter;
import ru.test.task.models.Interest;

import java.util.List;


@Getter

public class PutUserDTO extends GetUserDTO {
    String password;

    public PutUserDTO(
            Integer id,
            String fullName,
            String userName,
            String email,
            List<Interest> interests,
            String password
    ) {
        super(id, fullName, userName, email, interests);
        this.password = password;
    }

}