package com.jaemin.querydsl.repository;

import com.jaemin.querydsl.dto.MemberSearchCondition;
import com.jaemin.querydsl.dto.MemberTeamDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberRepositoryCustom {

    List<MemberTeamDto> search(MemberSearchCondition condition);
    // count 쿼리 data 쿼리를 한번에
    Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable);
    // count 쿼리 data 쿼리를 나눠서
    Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable);
}
