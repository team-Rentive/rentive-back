package com.rent.rentservice.post.domain;

import com.rent.rentservice.category.domain.PostCategory;
import com.rent.rentservice.user.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
public class PostEditor {

    private User userId;
    private int favorite;
    private int viewCount;
    private PostCategory category;
    private boolean deleteCheck = Boolean.FALSE;
    private String title;
    private String text;

    @Builder
    public PostEditor(User userId,
                      String title,
                      int favorite,
                      String text,
                      int viewCount) {
        this.userId = userId;
        this.title = title;
        this.favorite = favorite;
        this.text = text;
        this.viewCount = viewCount;
    }
}
