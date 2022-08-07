package com.jaemin.querydsl.repository;

import com.jaemin.querydsl.dto.MemberSearchCondition;
import com.jaemin.querydsl.dto.MemberTeamDto;
import com.jaemin.querydsl.entity.Member;
import com.jaemin.querydsl.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class MemberJpaRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    void basic_test() {
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);

        List<Member> result1 = memberJpaRepository.findAll_querydsl();
        assertThat(result1).containsExactly(member);

        List<Member> result2 = memberJpaRepository.findByUsername(member.getUsername());
        assertThat(result2).containsExactly(member);
    }

    @Test
    public void searchByBuilder() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);


        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);

        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        MemberSearchCondition searchCondition = new MemberSearchCondition();
        searchCondition.setAgeGoe(35);
        searchCondition.setAgeLoe(40);
        searchCondition.setTeamName("teamB");

//        List<MemberTeamDto> result = memberJpaRepository.searchByBuilder(searchCondition);
        List<MemberTeamDto> result = memberJpaRepository.search(searchCondition);
        assertThat(result).extracting("username").containsExactly("member4");

    }


}