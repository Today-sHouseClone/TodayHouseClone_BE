package com.hanghae.Today.sHouse.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "HEARTCHECK_A",
        sequenceName = "HEARTCHECK_B",
        initialValue = 1, allocationSize = 50)
public class HeartCheck {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "HEARTCHECK_A")
    @Column(name = "HEARTCHECK_ID")
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_REVIEW_ID")
    private Post post;

    @Column
    private Boolean postLikeStatus;

    public HeartCheck(User user, Post post, Boolean postLikeStatus) {
        this.user = user;
        this.post = post;
        this.postLikeStatus = postLikeStatus;
    }

}
