package com.rent.rentservice.post.domain;

import com.querydsl.core.annotations.QueryProjection;
import com.rent.rentservice.category.domain.Category;
import com.rent.rentservice.user.domain.User;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Where(clause = "deleteCheck IS FALSE")
@SQLDelete(sql = "UPDATE post SET deleteCheck = true WHERE postId=?")
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postID;
    @ManyToOne
    @JoinColumn(name = "USERID")
    private User userID;
    private String title;
    @CreationTimestamp @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;

//    @CreationTimestamp @Temporal(TemporalType.TIMESTAMP)
//    private Date limitDate; //todo 달력으로 시작일 종료일 변경

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    private int favorite;
    @Column(columnDefinition = "TEXT")
    private String text;
    private int viewCount;

    private boolean deleteCheck = Boolean.FALSE;

    protected Post() {}
    @Builder @QueryProjection
    public Post(User userID,
                String title,
                int favorite,
                String text,
                int viewCount,
                Category category) {
        this.userID = userID;
        this.title = title;
        this.favorite = favorite;
        this.text = text;
        this.viewCount = viewCount;
        this.category = category;
    }
}
