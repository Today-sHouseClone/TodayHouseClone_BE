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
public class CommentCheck {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "COMMENTCHECK_ID")
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMMENT_ID")
    private Comment comment;

    @Column
    private Boolean commentHeartStatus;

    public CommentCheck(User user, Comment comment, Boolean commentHeartStatus) {
        this.user = user;
        this.comment = comment;
        this.commentHeartStatus = commentHeartStatus;
    }
    public void update(CommentCheck commentHeartStatus){
        this.commentHeartStatus = commentHeartStatus.getCommentHeartStatus();
    }
}
