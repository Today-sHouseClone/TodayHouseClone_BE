package com.hanghae.Today.sHouse.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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

    @Column
    private String comment;

    @JsonManagedReference
    @OneToMany(mappedBy = "comment", orphanRemoval = true)
    private List<CommentCheck> commentCheck;

    @Column
    private Boolean commentHeartStatus;

    @JsonBackReference  // 순환참조 방지
    @ManyToOne(fetch = FetchType.LAZY)
    //@JsonIgnore
    @JoinColumn(name ="POST_ID")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name ="USER_ID")
    private User user;


    @Builder
    public Comment(User user, Post post, String getComment) {
        this.user = user;
        this.post = post;
        this.comment = getComment;
    }

    public Comment(Boolean commentHeartStatus, User user, Long id, Post post, String comment) {
        this.commentHeartStatus = commentHeartStatus;
        this.user = user;
        this.id = id;
        this.post = post;
        this.comment = comment;
    }
}
