package com.jaemin.querydsl.repository;

import com.jaemin.querydsl.dto.MemberSearchCondition;
import com.jaemin.querydsl.dto.MemberTeamDto;
import com.jaemin.querydsl.entity.Member;
import com.jaemin.querydsl.entity.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(value = "test")
@Transactional
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private JPAQueryFactory queryFactory;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void basic_test() {
        Member member = new Member("member1", 10);
        memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);

        List<Member> result1 = memberRepository.findAll();
        assertThat(result1).containsExactly(member);

        List<Member> result2 = memberRepository.findByUsername(member.getUsername());
        assertThat(result2).containsExactly(member);
    }

    @Test
    public void search() {
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

        List<MemberTeamDto> result = memberRepository.search(searchCondition);
        assertThat(result).extracting("username").containsExactly("member4");
    }

    @Test
    public void searchPageSimple() {
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

        PageRequest pageRequest = PageRequest.of(0, 3);


        Page<MemberTeamDto> result = memberRepository.searchPageSimple(searchCondition, pageRequest);


        assertThat(result.getSize()).isEqualTo(3);
        assertThat(result.getContent()).extracting("username").containsExactly("member1", "member2", "member3");
    }



}
