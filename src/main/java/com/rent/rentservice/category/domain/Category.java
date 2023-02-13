package com.rent.rentservice.category.domain;

import com.rent.rentservice.post.domain.Post;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    private String categoryValue;

    @OneToMany
    private List<Post> postList = new ArrayList<>();
}
