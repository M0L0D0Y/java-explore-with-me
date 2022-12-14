package ru.practicum.ewm.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email
    @NotBlank(message = "Нет email пользователя")
    @Column(name = "email", unique = true)
    private String email;
    @NotBlank(message = "Нет имени пользователя")
    @Size(max = 40, message = "Превышен лимит символов")
    @Column(name = "name")
    private String name;
    //Для подписок
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "friends",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "friend_id")}
    )
    @ToString.Exclude
    private Set<User> friends = new HashSet<>();
}

