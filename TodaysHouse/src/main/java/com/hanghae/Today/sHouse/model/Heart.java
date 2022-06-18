package com.hanghae.Today.sHouse.model;

import com.hanghae.Today.sHouse.dto.HeartDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "HEART_A",
        sequenceName = "HEART_B",
        initialValue = 1, allocationSize = 50)
public class Heart {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "HEART_A")
    @Column(name = "HEART_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    public Heart(HeartDto heartDto) {
        this.user = heartDto.getUser();
        this.post = heartDto.getPost();
    }
}