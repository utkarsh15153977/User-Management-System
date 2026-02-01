package com.user.usermanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.processing.Pattern;

@Entity
@Table(name = "users")  // Good practice to specify table name
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")  // Changed from "user-id" to "user_id"
    private Long id;  // Changed from int to Long (better for JPA)

    @Column(name = "name")
    private String name;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;  // Changed from "mail" to "email" for clarity

    @Column(name = "password")
    private String password;

    @Column(name = "mobile_number")  // Better naming
    private String mobileNumber;  // Changed from "mob_mum"

    @Enumerated(EnumType.STRING)
    private Role role;
}