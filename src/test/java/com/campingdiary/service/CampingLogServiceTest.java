package com.campingdiary.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.campingdiary.domain.CampingLog;
import com.campingdiary.repository.CampingLogRepository;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@SpringBootTest
class CampingLogServiceTest {

    @MockBean
    private CampingLogRepository campingLogRepository;

    @Autowired
    private CampingLogService campingLogService;

    @Test
    void findAll_캠핑로그_리스트_반환() {
        // given
        CampingLog mockLog = new CampingLog();
        mockLog.setMemo("테스트 캠핑");
        given(campingLogRepository.findAll()).willReturn(Collections.singletonList(mockLog));

        // when
        List<CampingLog> logs = campingLogService.findAll();

        // then
        assertThat(logs).isNotEmpty();
        assertThat(logs.get(0).getMemo()).isEqualTo("테스트 캠핑");
    }
}