package com.EEIT25.unicloth.controller.admin;

import com.EEIT25.unicloth.dto.admin.coupon.CouponRecordFilter;
import com.EEIT25.unicloth.enums.coupon.CouponSource;
import com.EEIT25.unicloth.enums.coupon.MemberCouponStatus;
import com.EEIT25.unicloth.service.admin.AdminCouponService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 只測 Controller：發券紀錄的網址參數有沒有正確填進 {@link CouponRecordFilter}。
 */
@WebMvcTest(AdminCouponController.class)
class AdminCouponControllerTest {

    @Autowired MockMvc mockMvc;

    @MockitoBean AdminCouponService adminCouponService;

    @Test
    void 發券紀錄_網址參數全部填進篩選DTO() throws Exception {
        when(adminCouponService.getRecords(any(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/admin/mkt/coupons/records")
                        .param("keyword", "amy")
                        .param("couponId", "3")
                        .param("source", "BIRTHDAY")
                        .param("status", "USED")
                        .param("receivedFrom", "2026-10-01")
                        .param("receivedTo", "2026-10-31")
                        .param("page", "2"))
                .andExpect(status().isOk());

        ArgumentCaptor<CouponRecordFilter> filterCaptor = ArgumentCaptor.forClass(CouponRecordFilter.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(adminCouponService).getRecords(filterCaptor.capture(), pageableCaptor.capture());

        assertThat(filterCaptor.getValue()).isEqualTo(new CouponRecordFilter(
                "amy", 3L, CouponSource.BIRTHDAY, MemberCouponStatus.USED,
                LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31)));
        assertThat(pageableCaptor.getValue().getPageNumber()).isEqualTo(2);
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(10);
    }

    @Test
    void 發券紀錄_沒帶參數時篩選全是null() throws Exception {
        when(adminCouponService.getRecords(any(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/admin/mkt/coupons/records"))
                .andExpect(status().isOk());

        ArgumentCaptor<CouponRecordFilter> filterCaptor = ArgumentCaptor.forClass(CouponRecordFilter.class);
        verify(adminCouponService).getRecords(filterCaptor.capture(), any());
        assertThat(filterCaptor.getValue()).isEqualTo(new CouponRecordFilter(null, null, null, null, null, null));
    }
}
