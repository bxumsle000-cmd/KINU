package com.EEIT25.unicloth.security;

import com.EEIT25.unicloth.entity.Staff;
import com.EEIT25.unicloth.enums.staff.Department;
import com.EEIT25.unicloth.enums.staff.StaffStatus;
import com.EEIT25.unicloth.repository.member.MemberProfileRepository;
import com.EEIT25.unicloth.repository.member.MemberRepository;
import com.EEIT25.unicloth.repository.staff.StaffRepository;
import com.EEIT25.unicloth.security.jwt.JwtProperties;
import com.EEIT25.unicloth.security.jwt.JwtService;
import com.EEIT25.unicloth.security.jwt.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 驗證 SecurityConfig 的放行規則與 JwtAuthFilter：哪些路徑不用登入、帶不帶 token 分別得到什麼狀態碼。
 * 用假的 Controller 模擬各種路徑，不依賴真正的 Controller / Service / 資料庫。
 */
@WebMvcTest(controllers = SecurityConfigTest.DummyController.class)
@Import({SecurityConfig.class, JwtService.class, CurrentMember.class, CurrentStaff.class, SecurityConfigTest.DummyController.class})
@EnableConfigurationProperties(JwtProperties.class)
class SecurityConfigTest {

    @Autowired MockMvc mockMvc;
    @Autowired JwtService jwtService;

    // CurrentMember 需要它；這裡只測 getCurrentId()，不會真的查資料庫
    @MockitoBean MemberRepository memberRepository;
    // SecurityConfig 用它判斷「補填過資料沒」；預設每個會員都填過，要測沒填的情況再個別改
    @MockitoBean MemberProfileRepository memberProfileRepository;
    // JwtAuthFilter 用它確認員工存在且沒被停用（staffToken() 會順便設定好）；CurrentStaff 也需要它
    @MockitoBean StaffRepository staffRepository;

    @BeforeEach
    void allMembersHaveProfile() {
        when(memberProfileRepository.existsById(anyLong())).thenReturn(true);
    }

    @RestController
    static class DummyController {
        @Autowired CurrentMember currentMember;

        @GetMapping("/api/cart") String cart() { return "cart"; }
        @GetMapping("/api/products") String products() { return "products"; }
        @PostMapping("/api/products") String createProduct() { return "created"; }
        @PostMapping("/api/auth/firebase") String login() { return "ok"; }
        @GetMapping("/api/member/me") String me() { return "me"; }
        @PutMapping("/api/member/me/complete-profile") String completeProfile() { return "completed"; }
        /** 回傳 CurrentMember 認為的「現在是誰」，驗證整條鏈：Filter → SecurityContext → CurrentMember */
        @GetMapping("/api/whoami") Long whoami() { return currentMember.getCurrentId(); }

        @Autowired CurrentStaff currentStaff;

        @PostMapping("/api/admin/auth/login") String adminLogin() { return "ok"; }
        @GetMapping("/api/admin/product/items") String adminProductItems() { return "product-items"; }
        @GetMapping("/api/admin/mkt/coupons") String adminMktCoupons() { return "mkt-coupons"; }
        @GetMapping("/api/admin/admin/staff") String adminAdminStaff() { return "admin-staff"; }
        /** 回傳 CurrentStaff 認為的「現在是哪個員工」 */
        @GetMapping("/api/admin/whoami") Long adminWhoami() { return currentStaff.getCurrentId(); }
    }

    /** 商品部員工的 token；同時假裝資料庫裡有這位 ACTIVE 的員工（JwtAuthFilter 會回資料庫確認） */
    private String staffToken(Long staffId) {
        return staffToken(staffId, Department.PRODUCT);
    }

    private String staffToken(Long staffId, Department department) {
        givenStaff(staffId, department, StaffStatus.ACTIVE);
        return jwtService.generateStaffToken(staffId, department);
    }

    /** 假裝資料庫裡有這位員工 */
    private void givenStaff(Long staffId, Department department, StaffStatus status) {
        when(staffRepository.findById(staffId)).thenReturn(Optional.of(Staff.builder()
                .id(staffId).account("staff" + staffId).passwordHash("x").name("測試員工")
                .department(department).status(status).build()));
    }

    // ===== 員工 / 後台 =====

    @Test
    void 員工登入端點_沒帶token_放行() throws Exception {
        mockMvc.perform(post("/api/admin/auth/login"))
                .andExpect(status().isOk());
    }

    @Test
    void 後台API_沒帶token_401() throws Exception {
        mockMvc.perform(get("/api/admin/product/items"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 後台API_帶員工token_200_CurrentStaff取得的是token裡的員工id() throws Exception {
        mockMvc.perform(get("/api/admin/product/items").header("Authorization", "Bearer " + staffToken(3L)))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/admin/whoami").header("Authorization", "Bearer " + staffToken(3L)))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }

    @Test
    void 部門API_別的部門的員工_403沒有權限() throws Exception {
        // staffToken 是商品部，不能用行銷的 API
        mockMvc.perform(get("/api/admin/mkt/coupons").header("Authorization", "Bearer " + staffToken(3L)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("沒有權限使用這個功能"));
    }

    @Test
    void 部門API_ADMIN員工_所有部門都能用() throws Exception {
        String token = staffToken(5L, Department.ADMIN);

        for (String path : new String[]{"/api/admin/product/items", "/api/admin/mkt/coupons", "/api/admin/admin/staff"}) {
            mockMvc.perform(get(path).header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void ADMIN的API_其他部門的員工_403沒有權限() throws Exception {
        mockMvc.perform(get("/api/admin/admin/staff").header("Authorization", "Bearer " + staffToken(3L)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("沒有權限使用這個功能"));
    }

    @Test
    void 後台API_帶會員token_403沒有權限() throws Exception {
        String token = jwtService.generateToken(1L);

        mockMvc.perform(get("/api/admin/product/items").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("沒有權限使用這個功能"));
    }

    @Test
    void 會員API_帶員工token_403_不會被當成同id的會員() throws Exception {
        String token = staffToken(1L);

        for (String path : new String[]{"/api/cart", "/api/whoami", "/api/member/me"}) {
            mockMvc.perform(get(path).header("Authorization", "Bearer " + token))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.message").value("沒有權限使用這個功能"));
        }
        mockMvc.perform(put("/api/member/me/complete-profile").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void 員工被停用_token還沒過期_後台API一樣401() throws Exception {
        String token = staffToken(3L);
        givenStaff(3L, Department.PRODUCT, StaffStatus.DISABLED);

        mockMvc.perform(get("/api/admin/product/items").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 員工已經不存在_後台API401() throws Exception {
        String token = jwtService.generateStaffToken(99L, Department.ADMIN);
        when(staffRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/admin/product/items").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 部門以資料庫為準_token裡的舊部門不算() throws Exception {
        // token 發的時候是 ADMIN，之後資料庫改成商品部：不能再用 ADMIN 的 API，商品部的可以
        String token = jwtService.generateStaffToken(7L, Department.ADMIN);
        givenStaff(7L, Department.PRODUCT, StaffStatus.ACTIVE);

        mockMvc.perform(get("/api/admin/admin/staff").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/api/admin/product/items").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void 員工token_看商品不受影響() throws Exception {
        mockMvc.perform(get("/api/products").header("Authorization", "Bearer " + staffToken(1L)))
                .andExpect(status().isOk());
    }

    @Test
    void 要登入的API_沒帶token_401且回ErrorResponse格式() throws Exception {
        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("尚未登入或登入已失效"));
    }

    @Test
    void 要登入的API_帶有效token_200() throws Exception {
        String token = jwtService.generateToken(1L);

        mockMvc.perform(get("/api/cart").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void 要登入的API_帶無效token_401() throws Exception {
        mockMvc.perform(get("/api/cart").header("Authorization", "Bearer not.a.token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 要登入的API_帶別的secret簽的token_401() throws Exception {
        String foreign = new JwtService(new JwtProperties("another-secret-another-secret-another", 60_000))
                .generateToken(1L);

        mockMvc.perform(get("/api/cart").header("Authorization", "Bearer " + foreign))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 帶token_CurrentMember取得的是token裡的會員id() throws Exception {
        String token = jwtService.generateToken(7L);

        mockMvc.perform(get("/api/whoami").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("7"));
    }

    @Test
    void 看商品_沒帶token_放行() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());
    }

    @Test
    void 商品只放行GET_其他方法沒帶token_401() throws Exception {
        mockMvc.perform(post("/api/products"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 登入端點_沒帶token_放行() throws Exception {
        mockMvc.perform(post("/api/auth/firebase"))
                .andExpect(status().isOk());
    }

    @Test
    void api以外的路徑_沒帶token_不會是401() throws Exception {
        // 切片測試沒有這些檔案，會是 404；重點是「沒被 Security 擋成 401」
        for (String path : new String[]{"/img/products/a.jpg", "/video/a.mp4", "/swagger-ui/index.html", "/v3/api-docs"}) {
            int status = mockMvc.perform(get(path)).andReturn().getResponse().getStatus();
            assertThat(status).as(path).isNotEqualTo(401);
        }
    }

    @Test
    void 沒補填資料_其他要登入的API_403且回ErrorResponse格式() throws Exception {
        when(memberProfileRepository.existsById(9L)).thenReturn(false);
        String token = jwtService.generateToken(9L);

        mockMvc.perform(get("/api/cart").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(403))
                .andExpect(jsonPath("$.message").value("請先填寫會員資料"));
    }

    @Test
    void 沒補填資料_還是可以查自己的資料和補填() throws Exception {
        when(memberProfileRepository.existsById(9L)).thenReturn(false);
        String token = jwtService.generateToken(9L);

        mockMvc.perform(get("/api/member/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/member/me/complete-profile").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void 查自己的資料_沒帶token還是401() throws Exception {
        mockMvc.perform(get("/api/member/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 沒補填資料_看商品不受影響() throws Exception {
        when(memberProfileRepository.existsById(9L)).thenReturn(false);
        String token = jwtService.generateToken(9L);

        mockMvc.perform(get("/api/products").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
