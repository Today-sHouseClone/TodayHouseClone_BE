package com.hanghae.Today.sHouse.model;

import com.hanghae.Today.sHouse.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "POST_A",
        sequenceName = "POST_B",
        initialValue = 1, allocationSize = 50)
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "POST_A")
    @Column(name = "POST_ID")
    private Long id;

    @Column(nullable = false)
    private int size;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String style;

    @Column(nullable = false)
    private String area;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    private User user;

    public Post(User user, PostRequestDto requestDto) {
        this.user = user;
        this.size = requestDto.getSize();
        this.type = requestDto.getType();
        this.style = requestDto.getStyle();
        this.area = requestDto.getArea();
        this.imageUrl = requestDto.getImageUrl();
        this.content = requestDto.getContent();
    }

}
