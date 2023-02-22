package com.rent.rentservice.post.request;

import com.rent.rentservice.post.domain.Post;
import com.rent.rentservice.user.domain.User;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

//todo category
@Data
public class PostCreateForm {
    @NotBlank(message = "제목을 입력해 주세요")
    private String title;

    @NotBlank(message = "내용을 입력해 주세요")
    private String text;

    @NotBlank(message = "카테고리를 선택해주세요")
    private String categoryName;

    public PostCreateForm() {}
    @Builder
    public PostCreateForm(String title, String text, String categoryName) {
        this.title = title;
        this.text = text;
        this.categoryName = categoryName;
    }
}
