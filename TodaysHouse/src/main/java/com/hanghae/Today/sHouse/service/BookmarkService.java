package com.hanghae.Today.sHouse.service;

import com.hanghae.Today.sHouse.dto.BookmarkDto;
import com.hanghae.Today.sHouse.model.Bookmark;
import com.hanghae.Today.sHouse.model.Post;
import com.hanghae.Today.sHouse.model.User;
import com.hanghae.Today.sHouse.repository.BookmarkRepository;
import com.hanghae.Today.sHouse.repository.PostRepository;
import com.hanghae.Today.sHouse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookmarkService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final BookmarkRepository bookmarkRepository;

    //좋아요 누르기
    @Transactional
    public boolean clickToBookmark(Long postId, Long userId) {
        Post post = getPost(postId);
        User user = getUser(userId);

        boolean toggleBookmark;

        BookmarkDto bookmarkDto = new BookmarkDto(post, user);
        Bookmark bookmark = new Bookmark(bookmarkDto);
        int BookmarkCnt = bookmark.getPost().getBookmarkCnt();

        //지금 로그인 되어있는 사용자가 해당 포스트에 좋아요를 누른적이 있냐 없냐.
        Bookmark findBookmark = bookmarkRepository.findByPostAndUser(post, user).orElse(null);

        if(findBookmark == null){
            bookmark.getPost().setBookmarkCnt(BookmarkCnt+1);

            bookmarkRepository.save(bookmark);
            toggleBookmark = true;
        }
        else{
            bookmark.getPost().setBookmarkCnt(BookmarkCnt-1);

            bookmarkRepository.deleteById(findBookmark.getId());
            toggleBookmark = false;
        }
        return toggleBookmark;
    }


    private User getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자 정보가 존재하지 않습니다.")
        );
        return user;
    }

    private Post getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()->new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        return post;
    }
}
