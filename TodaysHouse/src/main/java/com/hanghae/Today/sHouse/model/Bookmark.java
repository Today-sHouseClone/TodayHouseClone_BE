package com.hanghae.Today.sHouse.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hanghae.Today.sHouse.dto.BookmarkDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "BOOKMARK_A",
        sequenceName = "BOOKMARK_B",
        initialValue = 1, allocationSize = 50)
public class Bookmark {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "BOOKMARK_A")
    @Column(name = "BOOKMARK_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    @Column
    private Boolean bookmarkStatus;

    public Bookmark(User user, Post post, Boolean bookmarkStatus) {
        this.user = user;
        this.post = post;
        this.bookmarkStatus = bookmarkStatus;
    }
}