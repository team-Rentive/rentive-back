package com.rent.rentservice.post.service;

import com.rent.rentservice.post.domain.Post;
import com.rent.rentservice.post.domain.PostEditor;
import com.rent.rentservice.post.exception.SessionNotFoundException;
import com.rent.rentservice.post.repository.PostRepository;
import com.rent.rentservice.post.request.PostCreateForm;
import com.rent.rentservice.post.request.PostUpdateForm;
import com.rent.rentservice.user.domain.User;
import com.rent.rentservice.user.exception.UserNotFoundException;
import com.rent.rentservice.user.repository.UserRepository;
import com.rent.rentservice.post.request.SearchForm;
import com.rent.rentservice.util.session.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rent.rentservice.util.session.SessionUtil.checkPostAuth;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 전체 게시글 조회
    public void allItem() {
        postRepository.findAll();
    }

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

    // 검색에 따른 게시글 조회
    @Transactional
    public List<Post> findBySearch(SearchForm condition) {
        List<Post> searchPostList = postRepository.findBySearchUsingQueryDsl(condition);
        return searchPostList;
    }

    // 게시글 상세 조회
    public Post postDetail(Long requestID){
        Post post = postRepository.findById(requestID)
                .orElse(null);

        post.viewCountUp(post.getViewCount() + 1);
        return post;
    }

    // 게시글 업데이트
    @Transactional
    public void update(Long id, PostUpdateForm postUpdateForm, HttpSession session) {

        // 게시글 조회
        Post post = postRepository.findById(id)
                .orElse(null);

        // 세션에 등록된 사용자의 게시글에 수정 삭제 권한 부여 -> 예외처리
        checkPostAuth(session, post);

        // postEditor 객체 생성
        PostEditor postEditor = PostEditor.builder()
                .title(postUpdateForm.getTitle())
                .text(postUpdateForm.getText())
                .favorite(post.getFavorite())
                .viewCount(post.getViewCount())
                .build();

        // Update
        post.updatePost(postEditor);

    }

    // 게시글 삭제
    public void delete(Long id, HttpSession session) {
        Post post = postRepository.findById(id).orElse(null);

        // 세션에 등록된 사용자의 게시글에 수정 삭제 권한 부여 -> 예외처리
        checkPostAuth(session, post);

        postRepository.deleteById(id);
    }
//    /**
//     * @description build the post constructor
//     * @param post
//     * @return post builder
//     */
//    private Post convertEntityInPost(@NotNull Post post) {
//        return post.builder()
//                .title(post.getTitle())
//                .favorite(post.getFavorite())
//                .text(post.getText())
//                .build();
//    }
//
//    /**
//     * @description Find all post by post ID
//     * @param ID
//     * @return new post object
//     */
//    public List<Post> findByID(Long ID) {
//        List<Post> postList = postRepository.findByPostID(ID);
//        List<Post> postListByID = new ArrayList<>();
//
//        if(postList.isEmpty()) return postListByID; //todo exception 처리
//
//        for(Post post : postList) {
//            postListByID.add(this.convertEntityInPost(post));
//        }
//
//        return postListByID;
//    }
}
