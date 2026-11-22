package com.EEIT25.unicloth.dto.admin.coupon;

import com.EEIT25.unicloth.enums.coupon.CouponSource;
import com.EEIT25.unicloth.enums.coupon.MemberCouponStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 發券紀錄的篩選條件（GET 的網址參數，Spring 會依同名參數自動填入），每個都是選填，null 就不篩
 *
 * @param keyword      比對會員姓名或 email（不分大小寫、部分符合就算），空白也當作不篩
 * @param couponId     哪一張折價券
 * @param source       券怎麼來的（MANUAL / BIRTHDAY…）
 * @param status       USABLE / USED / EXPIRED
 * @param receivedFrom 發券日期起（含），格式 2026-10-01
 * @param receivedTo   發券日期迄（含），格式 2026-10-31
 */
public record CouponRecordFilter(
        String keyword,
        Long couponId,
        CouponSource source,
        MemberCouponStatus status,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate receivedFrom,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate receivedTo
) {
}
