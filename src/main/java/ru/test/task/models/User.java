package ru.test.task.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"user\"")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotEmpty(message = "full name can't be empty")
    @Column(name = "full_name",
            nullable = false)
    @Size(min = 10, message = "Invalid size of full name")
    private String fullName;

    @Column(name = "username",
            nullable = false)
    @NotEmpty(message = "username can't be empty")
    @Size(min = 3, message = "Invalid size of username")
    private String userName;

    @NotEmpty(message = "email can't be empty")
    @Email(message = "Invalid email")
    @Column(name = "email",
            nullable = false)
    private String email;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "users_interests_mapping",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id")
    )
    private List<Interest> interests;
    @NotEmpty(message = "password can't be empty")
    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn
    private Role role;
}
