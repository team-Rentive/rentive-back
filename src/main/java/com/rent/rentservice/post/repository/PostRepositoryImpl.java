package com.rent.rentservice.post.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rent.rentservice.post.domain.Post;
import com.rent.rentservice.post.request.SearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import com.rent.rentservice.util.queryCustom.SearchUtil;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.rent.rentservice.post.domain.QPost.post;

/**
 * @description QueryDsl�� �̿��� ������ �ۼ��� Ŭ����
 * @description Implements PostRepositoryCustom
 * @author �����
 * @since 23.01.20
 */

@Repository
public class PostRepositoryImpl implements PostRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    // �˻� custom method
    @Override
    public List<Post> findBySearchUsingQueryDsl(SearchForm condition) {
        // ������ where ������ + �ð��� ���� ��������
        return jpaQueryFactory
                .selectFrom(post)
                .where(SearchUtil.isSearchable(condition.getContent(), condition.getType()))
                .fetch();
    }

    // ������ ��ȸ�� ���� �޼ҵ�
    @Override
    public Post updateViewCount(Long requestId) {
        jpaQueryFactory
                .update(post)
                .set(post.viewCount, post.viewCount.add(1))
                .where(post.postID.eq(requestId))
                .execute();

        return jpaQueryFactory
                .selectFrom(post)
                .where(post.postID.eq(requestId))
                .fetchOne();
    }

    @Override
    public PageImpl<Post> findByOffsetPaging(Pageable pageable) {
        List<Post> result = jpaQueryFactory
                .select(post)
                .from(post)
                .orderBy(PostSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(result, pageable, result.size());
    }

    private OrderSpecifier<?> PostSort(Pageable pageable) {
        // Pageable 정렬 조건 null 확인
        if(!pageable.getSort().isEmpty()) {
            // 정렬 값이 있다면 가져온다
            for(Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) { // PageRequest of() 확인
                    case "createAt":
                        return new OrderSpecifier(direction, post.regDate);
                    case "countOfFavorite":
                        return new OrderSpecifier(direction, post.favorite);
                    case "countOfViewCount":
                        return new OrderSpecifier(direction, post.viewCount);
                }
            }
        }
        return null;
    }
}