# 專案用到的註解（@XXX）整理

整理範圍：`src/main/java` 和 `src/test/java` 所有 `.java` 檔。
「次數」是整個專案出現的次數（含測試，也含少數寫在註解說明裡的），只是讓你知道哪些最常見，不是精確數字。

> **註解是誰的，看 `import` 就知道。**
> 例如 `import lombok.Getter;` → Lombok 的；`import jakarta.persistence.Column;` → JPA 的。

## 分類

| 檔案 | import 開頭 | 一句話 |
|------|-------------|--------|
| [1. Java 本身](1.Java本身.md) | （不用 import） | Java 語言內建 |
| [2. Spring Framework](2.Spring%20Framework.md) | `org.springframework.stereotype` / `context` / `beans` / `web` / `transaction` / `scheduling` / `format` / `core` | 建立物件、注入、Controller、交易 |
| [3. Spring Boot](3.Spring%20Boot.md) | `org.springframework.boot` | 啟動、讀設定、條件開關 |
| [4. Spring Data JPA](4.Spring%20Data%20JPA.md) | `org.springframework.data` | Repository 查詢、分頁 |
| [5. Spring Security](5.Spring%20Security.md) | `org.springframework.security` | 登入、權限 |
| [6. JPA](6.JPA.md) | `jakarta.persistence` | Entity 對應資料表 |
| [7. Hibernate](7.Hibernate.md) | `org.hibernate.annotations` | JPA 的實作，額外功能 |
| [8. Bean Validation](8.Bean%20Validation.md) | `jakarta.validation` | 檢查前端送來的資料 |
| [9. Lombok](9.Lombok.md) | `lombok` | 編譯時自動產生 getter、建構子等程式碼 |
| [10. springdoc / Swagger](10.springdoc%20Swagger.md) | `org.springdoc` / `io.swagger.v3` | 產生 API 文件（Swagger UI） |
| [11. 測試](11.測試.md) | `org.junit.jupiter.api` / `org.springframework.boot.test` / `org.springframework.test` | 單元測試、在測試中啟動 Spring |

> **JWT 沒有註解。** 專案用的 JWT 函式庫是 `jjwt`（`pom.xml` 的 `jjwt-api`），它只提供一般類別和方法（在 `JwtService` 裡呼叫），沒有任何 `@XXX`。
> 跟 JWT 有關的類別（`JwtAuthFilter`、`SecurityConfig`、`JwtProperties`）身上的註解，都是 Spring 的。
