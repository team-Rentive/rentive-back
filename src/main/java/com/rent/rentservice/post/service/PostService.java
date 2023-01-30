package com.rent.rentservice.post.service;

import com.rent.rentservice.post.domain.Post;
import com.rent.rentservice.post.exception.SessionNotFoundException;
import com.rent.rentservice.post.repository.PostRepository;
import com.rent.rentservice.post.request.PostCreateForm;
import com.rent.rentservice.user.domain.User;
import com.rent.rentservice.user.exception.UserNotFoundException;
import com.rent.rentservice.user.repository.UserRepository;
import com.rent.rentservice.post.request.SearchForm;
import com.rent.rentservice.util.session.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 글 등록
    public void create(PostCreateForm request,HttpSession session) throws Exception{
        //세션 아웃 검사
        if(SessionUtil.checkValidSession(session)) {
            throw new SessionNotFoundException();
        }

        //회원 정보 조회 검사
        User loginSessionID = userRepository.findById(SessionUtil.getLoginMemberIdn(session))
                .orElseThrow(UserNotFoundException::new);

        Post post = Post.builder()
                .userID(loginSessionID)
                .title(request.getTitle())
                .text(request.getText())
                .favorite(0)
                .viewCount(0)
                .build();

        postRepository.save(post);
    }

    // 전체 게시글 조회
    public PageImpl<Post> getAll() {
        //todo
    }
    // 검색에 따른 게시글 조회
    @Transactional
    public List<Post> findBySearch(SearchForm condition) {
        List<Post> searchPostList = postRepository.findBySearchUsingQueryDsl(condition);
        return searchPostList;
    }

    // 게시글 상세 조회
    public Post postDetail(Long requestID){
        Post post = postRepository.updateViewCount(requestID);

        return post;
    }
}
