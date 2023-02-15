package com.rent.rentservice.post.domain;

import com.querydsl.core.annotations.QueryProjection;
import com.rent.rentservice.category.domain.PostCategory;
import com.rent.rentservice.user.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.springframework.data.repository.query.Param;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import java.util.Date;

//todo category

@Entity
@Data
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE post SET deleted = true WHERE postid = ?")
@NoArgsConstructor
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postID;
    @ManyToOne
    @JoinColumn(name = "USERID")
    private User userID;
    private String title;
    @CreationTimestamp @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;
    private int favorite;
    @Column(columnDefinition = "TEXT")
    private String text;
    private int viewCount;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private PostCategory category;

    private boolean deleted = Boolean.FALSE;

    @Builder @QueryProjection
    public Post(User userID,
                String title,
                int favorite,
                String text,
                int viewCount) {
        this.userID = userID;
        this.title = title;
        this.favorite = favorite;
        this.text = text;
        this.viewCount = viewCount;
    }

    public void updatePost(PostEditor postEditor) {
        this.title = postEditor.getTitle();
        this.text = postEditor.getText();
        this.userID = postEditor.getUserId();
        this.favorite = postEditor.getFavorite();
        this.viewCount = postEditor.getViewCount();
    }

    public void viewCountUp(int viewCount) {
        this.viewCount = viewCount;
    }
}
