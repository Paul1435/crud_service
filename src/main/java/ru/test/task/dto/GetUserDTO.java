package ru.test.task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.test.task.models.Interest;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class GetUserDTO {
    private Integer id;
    private String fullName;
    private String userName;
    private String email;
    private List<Interest> interests;

    public void setId(int id) {
        this.id = id;
    }
}
