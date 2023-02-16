package com.rent.rentservice.util.paging.request;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import org.springframework.data.domain.Sort.*;

@Getter
public class PageRequest {
    private int page = 1; // 이런슥으로 하면 page 와 size 를 정적으로 고정 시켜줌
    private int size = 10;
    private Direction direction = Direction.DESC;

    private String property = "createAt";

    public PageRequest() {}

    public void setProperty(String property) {
        this.property = property;
    }

    public void setPage(int page) {
        this.page = page <= 0 ? 1 : page;
    }

    public void setSize(int size) {
        int DEFULT_SIZE = 10;
        int MAX_SIZE = 50;
        this.size = size > MAX_SIZE ? DEFULT_SIZE : size;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public org.springframework.data.domain.PageRequest of() {
        return org.springframework.data.domain.PageRequest.of(page - 1, size, direction, property);
    }
}
