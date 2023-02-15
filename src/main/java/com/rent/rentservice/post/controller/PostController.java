package com.rent.rentservice.post.controller;

import com.rent.rentservice.post.domain.Post;
import com.rent.rentservice.post.repository.PostRepository;
import com.rent.rentservice.post.request.PostCreateForm;
import com.rent.rentservice.post.request.PostUpdateForm;
import com.rent.rentservice.post.request.SearchForm;
import com.rent.rentservice.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

import static com.rent.rentservice.util.session.SessionUtil.checkPostAuth;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;

    // 전체 조회
    @GetMapping(value = "/home/item-list")
    public void list() {
        postService.allItem();
    }

    // 검색에 따른 전체 조회
    @GetMapping(value = "/home/item-list?")
    public List<Post> searchList(@RequestParam SearchForm request) {
        return postService.findBySearch(request);
    }

    // 게시글 CREATE
    @PostMapping(value = "/home/item-list/post")
    public void create(@RequestBody @Valid PostCreateForm request,HttpSession session) throws Exception{
        postService.create(request, session);
    }

    // 아이템 상세 조회 + 조회수 증가
    @GetMapping(value = "/home/item-list/{id}")
    public Post item_detail(@PathVariable("id") Long request) {
        return postService.postDetail(request);
    }

    // 아이템 업데이트
    @PatchMapping(value = "/home/item-list/update-{id}")
    public void itemUpdate(@RequestBody PostUpdateForm postUpdateForm, @PathVariable("id") Long id,HttpSession session) throws Exception{
        postService.update(id, postUpdateForm, session);
    }

    // 아이템 삭제
    @DeleteMapping(value = "/home/item-list/delete-{id}")
    public void itemDelete(@PathVariable("id") Long id, HttpSession session) {
        postService.delete(id, session);
    }

}
