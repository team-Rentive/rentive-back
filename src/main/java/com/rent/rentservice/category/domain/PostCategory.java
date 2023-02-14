package com.rent.rentservice.category.domain;

import com.rent.rentservice.post.domain.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name ="Category")
public class PostCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    private String categoryName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category") // category 가 PK, post 가 FK
    private List<Post> postList = new ArrayList<>();

    public void addPostIntoCategory(Post post) {
        this.postList.add(post);
    }
}
