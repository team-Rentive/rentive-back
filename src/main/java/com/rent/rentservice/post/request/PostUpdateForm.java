package com.rent.rentservice.post.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @description Post Update Form
 * @author 김기현
 * @since 2023.02.07
 */
@Data
public class PostUpdateForm {

    //todo db에서 값을 불러와서 setting 해주기
    private String title;

    private String text;

    @Builder
    public PostUpdateForm(String title, String text) {
        this.text = text;
        this.title = title;
    }
}
