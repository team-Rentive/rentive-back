package com.rent.rentservice.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rent.rentservice.post.domain.Post;
import com.rent.rentservice.post.domain.QPost;
import com.rent.rentservice.post.request.SearchForm;
import com.rent.rentservice.util.search.SearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Supplier;

import static com.rent.rentservice.post.domain.QPost.post;

/**
 * @description QueryDsl�� �̿��� ������ �ۼ��� Ŭ����
 * @description Implements PostRepositoryCustom
 * @author �����
 * @since 23.01.20
 */

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findBySearchUsingQueryDsl(SearchForm condition) {
        QPost post = QPost.post;

        // ������ where ������ + �ð��� ���� ��������
        return jpaQueryFactory
                .selectFrom(post)
                .where(isSearchable(condition.getContent(), condition.getType()))
                .orderBy(post.regDate.desc())
                .fetch();
    }

    // null ���� ������ Safe �� ó��
    BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> f) {
        try {
            return new BooleanBuilder(f.get());
        } catch (Exception e) {
            return new BooleanBuilder();
        }
    }

    // �����, ����, ���뿡 �´� ������ ����
    BooleanBuilder userEqual(String content) {
        return nullSafeBuilder(() -> post.userID.nickName.eq(content));
    }
    BooleanBuilder titleEqual(String content) {
        return nullSafeBuilder(() -> post.title.eq(content));
    }
    BooleanBuilder contentEqual(String content) {
        return nullSafeBuilder(() -> post.text.eq(content));
    }

    // ������ isSearchable �޼ҵ�� �����丵
    BooleanBuilder isSearchable(String content, SearchType searchType) {
        if(searchType == SearchType.title) { // ����
            return titleEqual(content);
        }
        else if(searchType == SearchType.writer) { // �����
            return userEqual(content);
        }
        else { // ���� + ����
            return titleEqual(content).or(contentEqual(content));
        }
    }

}