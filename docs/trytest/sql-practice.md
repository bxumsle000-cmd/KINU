# Spring Data JPA 衍生查詢方法練習題（Unicloth）

> 只出題目，不給答案。
> 每題都**只靠方法名稱**完成（不准用 `@Query`），寫出 Repository 介面裡的方法簽章即可。
> 欄位名稱一律以 **Entity 的 Java 屬性名稱**為準（例如 `skuCode`，不是資料表的 `sku_code`）。

---

## 第 1 題：商品詳情頁用 slug 找商品

### 情境

前台商品網址長這樣：`/products/ultra-light-down-jacket`，後端要用網址最後那段（`slug`）找出商品。
找不到時要能讓 Controller 回 404，所以回傳型別要能表達「可能沒有」。

### 需求

- 用 `slug` 精準比對，找出**一筆**商品
- 回傳型別要能表達「可能找不到」

### 寫在哪

`ProductRepository`

### 會用到的屬性

- `Product.slug`（`String`）

---

## 第 2 題：後台「低庫存警示」清單

### 情境

倉管人員每天早上要先看到「快賣完的款式」，好決定要不要補貨。

### 需求

- 款式庫存 `stock` **小於** 傳進來的門檻值（例如 5）
- 只看所屬商品**上架中**的（`Product.status == ProductStatus.ON_SALE`）
- 庫存最少的排最前面；庫存一樣時，依**商品名稱**由 A 到 Z 排
- 回傳多筆

### 寫在哪

`ProductVariantRepository`

### 會用到的屬性

- `ProductVariant.stock`（`Integer`）
- `ProductVariant.product`（`Product`，多對一）
- `Product.status`（`ProductStatus`）
- `Product.name`（`String`）

> 提示：條件和排序都要「穿過」`product` 這個關聯去拿商品的屬性。

---

## 第 3 題：會員中心「我的訂單」依狀態篩選

### 情境

會員中心的訂單頁上方有分頁籤：「未付款」「已出貨」……
點某個頁籤時，只列出**這位會員**、**該狀態**的訂單，最新的在最上面。

### 需求

- 指定會員的 `id`（注意：`Order` 上是 `member` 關聯，不是直接的 `memberId` 欄位）
- 指定訂單狀態（`OrderStatus`）
- 依建立時間 `createdAt` **由新到舊**
- 回傳多筆

### 寫在哪

`OrderRepository`

### 會用到的屬性

- `Order.member`（`Member`，多對一）→ `Member.id`（`Long`）
- `Order.status`（`OrderStatus`）
- `Order.createdAt`（`LocalDateTime`）

---

## 第 4 題：前台搜尋「關鍵字 + 價格區間」

### 情境

使用者在搜尋框打「外套」，再把價格滑桿拉到 500 ~ 1500 元。

### 需求

- 商品名稱 `name` **包含**關鍵字（前後都可以有其他字）
- 價格 `price` 介於最低價與最高價之間（含兩端）
- 只看**上架中**的商品
- 依價格**由低到高**
- 回傳多筆

### 寫在哪

`ProductRepository`

### 會用到的屬性

- `Product.name`（`String`）
- `Product.price`（`Integer`）
- `Product.status`（`ProductStatus`）

> 進階（選做）：改成**不分大小寫**比對關鍵字，方法名稱要怎麼改？

---

## 第 5 題：首頁「新品上市」區塊 + 後台檢查重複

這題分兩小題，練「限制筆數」和「不撈整筆資料、只回答是／否或數量」。

### 5-1 首頁新品前 8 筆

- 只看 `newArrival` 為 **true** 的商品
- 只看**上架中**的
- 依 `id` **由大到小**（當作越新的越前面）
- **只取前 8 筆**
- 寫在 `ProductRepository`

> 提示：布林欄位有專用的關鍵字，不必傳參數進去。

### 5-2 後台新增款式前，檢查 SKU 有沒有重複

- 給一個 `skuCode`，只要回答「**資料庫裡有沒有**」（`boolean`），不要撈出整筆 entity
- 寫在 `ProductVariantRepository`

### 5-3 後台儀表板：某狀態的訂單有幾筆

- 給一個 `OrderStatus`，回傳**筆數**（`long`）
- 寫在 `OrderRepository`

### 會用到的屬性

- `Product.newArrival`（`boolean`）、`Product.status`、`Product.id`
- `ProductVariant.skuCode`（`String`）
- `Order.status`（`OrderStatus`）
