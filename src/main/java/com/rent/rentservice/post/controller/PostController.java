package com.rent.rentservice.post.controller;

import com.rent.rentservice.post.domain.Post;
import com.rent.rentservice.post.repository.PostRepository;
import com.rent.rentservice.post.request.PostCreateForm;
import com.rent.rentservice.post.request.PostUpdateForm;
import com.rent.rentservice.post.request.SearchForm;
import com.rent.rentservice.post.service.PostService;
import com.rent.rentservice.util.paging.request.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

import static com.rent.rentservice.util.session.SessionUtil.checkPostAuth;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:400", allowedHeaders = "*", allowCredentials = "true")
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;

    // 전체 조회
    @GetMapping(value = "/home")
    public void list() {
        postService.allItem();
    }

    // 전체 조회 (Using Paging Custom)
    @GetMapping(value = "/api/v1/itemList")
    public PageImpl<Post> getAll(PageRequest pageRequest) {
        Pageable pageable = pageRequest.of();
        return postService.allPagingItem(pageable);
    }

    // 검색에 따른 전체 조회
    @GetMapping(value = "/api/v1/itemSearch/search")
    public List<Post> searchList(@RequestParam SearchForm request) {
        return postService.findBySearch(request);
    }

    // 게시글 CREATE
    @PostMapping(value = "/api/v1/itemCreate")
    public void create(@RequestBody @Valid PostCreateForm request,HttpSession session) throws Exception{
        postService.create(request, session);
    }

    // 아이템 상세 조회 + 조회수 증가
    @GetMapping(value = "/api/v1/itemDetail/{id}")
    public Post item_detail(@PathVariable("id") Long request) {
        return postService.postDetail(request);
    }

    // 아이템 업데이트
    @PatchMapping(value = "/api/v1/itemUpdate/{id}")
    public void itemUpdate(@RequestBody PostUpdateForm postUpdateForm, @PathVariable("id") Long id,HttpSession session) throws Exception{
        postService.update(id, postUpdateForm, session);
    }

    // 아이템 삭제
    @DeleteMapping(value = "/api/v1/itemDelete/{id}")
    public void itemDelete(@PathVariable("id") Long id, HttpSession session) {
        postService.delete(id, session);
    }

}
