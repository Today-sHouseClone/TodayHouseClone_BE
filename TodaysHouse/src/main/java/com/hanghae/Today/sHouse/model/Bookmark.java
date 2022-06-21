package com.hanghae.Today.sHouse.model;

import com.hanghae.Today.sHouse.dto.BookmarkDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

    public Bookmark(BookmarkDto bookmarkDto) {
        this.user = bookmarkDto.getUser();
        this.post = bookmarkDto.getPost();
    }
}