package com.hanghae.Today.sHouse.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "COMMENT_A",
        sequenceName = "COMMENT_B",
        initialValue = 1, allocationSize = 50)
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "COMMENT_A")
    @Column(name = "COMMENT_ID")
    private Long id;

    @Column(nullable = false)
    private String comment;

    @JsonBackReference  // 순환참조 방지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="POST_ID")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name ="USER_ID")
    private User user;

    public Comment(User user, Post post, String getComment) {
        this.user = user;
        this.post = post;
        this.comment = getComment;
    }
}
