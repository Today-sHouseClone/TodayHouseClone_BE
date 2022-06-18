package com.hanghae.Today.sHouse.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hanghae.Today.sHouse.dto.MultipartFileDto;
import com.hanghae.Today.sHouse.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

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

    @Column
    private int size;

    @Column
    private String type;

    @Column
    private String style;

    @Column(nullable = false)
    private String area;

    @Column(nullable = false)
    private String imageUrl;

    @Column
    private String content;

    @Column
    private int heartCnt;

    @Column
    private int bookmarkCnt;

    @Column
    private int commentCnt;

    @Column
    private int viewCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    private User user;

    @JsonManagedReference // 직렬화 허용 어노테이션
    @OneToMany(mappedBy = "post", orphanRemoval = true) // orpahRemanal = true 부모 삭제시 자식도 삭제
    private List<Comment> comments;

    public Post(User user, PostRequestDto requestDto) {
        this.user = user;
        this.size = requestDto.getSize();
        this.type = requestDto.getType();
        this.style = requestDto.getStyle();
        this.area = requestDto.getArea();
        this.imageUrl = requestDto.getImageUrl();
        this.content = requestDto.getContent();
    }

    public void update(User user, PostRequestDto postRequestDto) {
        this.user = user;
        this.size = postRequestDto.getSize();
        this.type = postRequestDto.getType();
        this.style = postRequestDto.getStyle();
        this.area = postRequestDto.getArea();
        this.imageUrl = postRequestDto.getImageUrl();
        this.content = postRequestDto.getContent();
    }
}
