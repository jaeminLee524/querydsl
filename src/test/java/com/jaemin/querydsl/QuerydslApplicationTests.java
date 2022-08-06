package com.jaemin.querydsl;

import com.jaemin.querydsl.entity.Hello;
import com.jaemin.querydsl.entity.QHello;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Transactional
@SpringBootTest
class QuerydslApplicationTests {

    @Autowired
    EntityManager em;

    @Test
    void contextLoads() {
        Hello hello1 = new Hello();
        em.persist(hello1);

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);

        QHello qHello = new QHello("hi");

        Hello hello = jpaQueryFactory
                .selectFrom(qHello)
                .fetchOne();

        Assertions.assertThat(hello).isEqualTo(hello1);

    }

}
