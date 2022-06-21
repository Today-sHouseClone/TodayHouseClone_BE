package com.hanghae.Today.sHouse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hanghae.Today.sHouse.dto.PostRequestDto;
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
        name = "POST_A",
        sequenceName = "POST_B",
        initialValue = 1, allocationSize = 50)
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "POST_A")
    @Column(name = "POST_ID")
    private Long id;

    @Column
    private String size;

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

    /*
    * //////////////////1. LAZY를 사용해서 지연로딩 ->
    *
    **오류 내용**
    * No serializer found for class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor and no properties discovered to create BeanSerializer
    * 필요가 없으면 조회를 안해서 비어있는 객체를 serializer 하려고 해서 발생되는 문제.
    *
    1. application 파일에 spring.jackson.serialization.fail-on-empty-beans=false 설정해주기
    2. 오류가 나는 엔티티의 LAZY 설정을 EAGER로 바꿔주기
    3. 오류가 나는 컬럼에 @JsonIgnore를 설정해주기
    4. @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    *
    * JsonIgnore 단점은? 남용했을 때.
    *
    * ///////////////////2. limit
    *
    * 2-1)@Query("select p from Post p order by p.viewCnt desc")

      2-2)@Query("select new com.hanghae.Today.sHouse.dto.PostResponseDto(p.id, p.size, p.type, p.style, p.area, p.imageUrl, p.content, p.createdAt, p.modifiedAt)" +
            " from Post p order by p.viewCnt desc")

      2-3)nativeQuery = true -> LIMIT 사용
       
      2-4)지금처럼
    * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name="USER_ID")
    private User user;

    //@JsonManagedReference // 직렬화 허용 어노테이션
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
