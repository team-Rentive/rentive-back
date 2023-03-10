package com.rent.rentservice.post.service;

import com.rent.rentservice.post.domain.Post;
import com.rent.rentservice.post.exception.NonePostException;
import com.rent.rentservice.post.exception.SessionNotFoundException;
import com.rent.rentservice.post.exception.UpdatePostSessionException;
import com.rent.rentservice.post.repository.PostRepository;
import com.rent.rentservice.post.repository.PostRepositoryImpl;
import com.rent.rentservice.post.request.PostCreateForm;
import com.rent.rentservice.post.request.PostUpdateForm;
import com.rent.rentservice.post.request.SearchForm;
import com.rent.rentservice.user.domain.User;
import com.rent.rentservice.user.repository.UserRepository;
import com.rent.rentservice.user.request.JoinForm;
import com.rent.rentservice.user.request.LoginForm;
import com.rent.rentservice.user.service.UserService;
import com.rent.rentservice.util.session.SessionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.List;

import static com.rent.rentservice.util.queryCustom.SearchType.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class PostServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;

    @Autowired
    PostRepositoryImpl postRepositoryImpl;
    @Autowired
    EntityManager em;

    MockHttpSession session;
    SessionUtil sessionUtil;

    @BeforeEach
    void clean() throws Exception {
        // ?????? ?????? ?????? ???????????? ????????? ?????????
        session = new MockHttpSession();
        JoinForm joinRequest = JoinForm.builder()
                .name("?????????")
                .nickName("?????????")
                .email("test@test.com")
                .phoneNumber("01012345678")
                .password("1234")
                .build();
        userService.join(joinRequest);

        LoginForm loginRequest = LoginForm.builder()
                .email("test@test.com")
                .password("1234")
                .build();
        userService.login(loginRequest, session);

        postRepository.deleteAll();
    }

    @Test @DisplayName("????????? ??????")
    void test1() throws Exception {
        //given
        PostCreateForm postRequest = PostCreateForm.builder()
                .title("?????? ?????????")
                .text("?????? ?????????")
                .build();

        //when
        postService.create(postRequest, session);

        //then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("?????? ?????????", post.getTitle());
        assertEquals(0, post.getFavorite());
        assertEquals("?????? ?????????", post.getText());
    }

    @Test @DisplayName("????????? ????????? ?????? ?????? ??????")
    void test2() throws Exception {
        // given
        PostCreateForm postRequest = PostCreateForm.builder()
                .title("?????? ?????????")
                .text("?????? ?????????")
                .build();

        // when
        session.invalidate();

        // todo ?????? then :: invalidate ????????? ?????? ?????? ???????????? ?????? ?????? ??????.
        assertThrows(SessionNotFoundException.class, () -> {
            postService.create(postRequest, session);
        });
    }

    @Test @DisplayName("???????????? ????????? ??????")
    void test3() throws Exception {
        // given
        // session ?????? ?????? ????????? ??????
        User user = userRepository.findById(sessionUtil.getLoginMemberIdn(session))
                .get();

        // post ?????????
        Post request1 = Post.builder()
                .title("?????? ????????? 1")
                .userID(user)
                .favorite(0)
                .text("????????? 1")
                .build();

        Post request2 = Post.builder()
                .title("?????? ????????? 2")
                .userID(user)
                .favorite(0)
                .text("?????? ????????? 2")
                .build();

        // post Repository ??? ??????
        postRepository.save(request1);
        postRepository.save(request2);


        // when
        List<Post> SearchedTitleList = postService
                .findBySearch(new SearchForm("??????", title));
        List<Post> SearchedWriterList = postService
                .findBySearch(new SearchForm("?????????", writer));
        List<Post> SearchedTitleContextList = postService
                .findBySearch(new SearchForm("??????", titleAndContext));

        // then
        assertThat(SearchedTitleContextList.size()).isEqualTo(1);
        assertThat(SearchedTitleList.size()).isEqualTo(2);
        assertThat(SearchedWriterList).extracting("title")
                .containsExactlyInAnyOrder("?????? ????????? 1", "?????? ????????? 2");
    }

    @Test @DisplayName("????????? ?????? ??????")
    void test4() throws Exception {
        // given
        PostCreateForm postRequest = PostCreateForm.builder()
                .title("??????")
                .text("??????")
                .build();

        postService.create(postRequest, session);

        Post post = postRepository.findAll().get(0);
        Long postId = post.getPostID();

        // when
        Post postDetail = postService.postDetail(postId);

        // then
        assertThat(post.getPostID()).isEqualTo(postDetail.getPostID());
        assertThat(post.getTitle()).isEqualTo(postDetail.getTitle());
    }

    @Test @DisplayName("????????? ?????? ??????")
    void test5() throws Exception {
        //given
        // session ?????? ?????? ????????? ??????
        User user = userRepository.findById(sessionUtil.getLoginMemberIdn(session))
                .orElse(null);

        // post ?????????
        Post request1 = Post.builder()
                .title("??????")
                .userID(user)
                .favorite(0)
                .text("??????")
                .viewCount(10)
                .build();

        // post Repository ??? ??????
        postRepository.save(request1);

        Post post = postRepository.findAll().get(0);
        Long postId = post.getPostID();

        //when
        Post postDetail = postService.postDetail(postId);

        //then
        //todo ????????? ?????? ??????
        assertThat(post.getViewCount()).isEqualTo(postDetail.getViewCount());

    }

    @Test @DisplayName("????????? ??????(????????????)")
    void test6() throws Exception{
        //given
        //????????? ??????
        PostCreateForm postRequest = PostCreateForm.builder()
                .title("?????? ?????????")
                .text("?????? ?????????")
                .build();

        postService.create(postRequest, session);

        //????????? id??? ??????
        Post existPost = postRepository.findAll().get(0);

        //updateForm
        PostUpdateForm postUpdateForm = PostUpdateForm.builder()
                .title("?????? ??????")
                .text("?????? ??????")
                .build();

        //when
        Post updatePost = postService.update(existPost.getPostID(), postUpdateForm, session);

        //then
        assertEquals(updatePost.getTitle(), postUpdateForm.getTitle());

    }

    @Test @DisplayName("????????? ?????? ??? ????????? ????????? ?????? ??????")
    void text7() throws Exception{
        //given
        //????????? ??????
        PostCreateForm postRequest = PostCreateForm.builder()
                .title("?????? ?????????")
                .text("?????? ?????????")
                .build();

        postService.create(postRequest, session);

        //????????? id??? ??????
        Post existPost = postRepository.findAll().get(0);

        //when
        postService.delete(existPost.getPostID(), session);

        //then
        assertThrows(NonePostException.class, () ->{
            postService.postDetail(existPost.getPostID());
        });
    }

    @Test @DisplayName("????????? ????????? ?????? ?????? ????????????")
    void test8() throws Exception{
        //given
        //????????? ??????
        PostCreateForm postRequest = PostCreateForm.builder()
                .title("?????? ?????????")
                .text("?????? ?????????")
                .build();

        postService.create(postRequest, session);
        Post existPost = postRepository.findAll().get(0);

        //updateForm
        PostUpdateForm postUpdateForm = PostUpdateForm.builder()
                .title("?????? ??????")
                .text("?????? ??????")
                .build();

        // ?????? ?????? ??????
        MockHttpSession tmpSession = new MockHttpSession();

        //expected
        assertThrows(UpdatePostSessionException.class, () -> {
            postService.update(existPost.getPostID(), postUpdateForm, tmpSession);
        });
    }
}
