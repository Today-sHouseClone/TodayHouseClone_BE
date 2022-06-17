package com.hanghae.Today.sHouse.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "USER_A",
        sequenceName = "USER_B",
        initialValue = 1, allocationSize = 50)
@Table(name="Users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "USER_A")
    @Column(name = "USER_ID")
    private Long id;

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String userNickname;
    @Column(nullable = false)
    private String password;

    public User(String username, String userNickname, String password) {
        this.username = username;
        this.userNickname = userNickname;
        this.password = password;
    }

}