# Category 架構

`pages/store/Category.jsx`（`/category/:code`）：分類商品列表，可篩選顏色、尺寸、價格和排序

```
Category
├─ Breadcrumb
├─ SubCategories
├─ Filters
├─ SortSelect
├─ ProductCard
└─ Pagination
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| Category | `pages/store/` | 頁面本體：依網址的分類代碼查分類資訊、篩選選項和商品；換分類清空篩選、換篩選回第 0 頁；條件連續變動時只用最後一次請求的結果 |
| Breadcrumb | `component/store/product/` | 麵包屑（例如 女裝 › 外套） |
| SubCategories | `component/store/product/` | 下一層分類按鈕，最底層分類不顯示 |
| Filters | `component/store/product/` | 篩選面板：顏色、尺寸、最低／最高價 |
| SortSelect | `component/store/product/` | 排序下拉選單 |
| ProductCard | `component/store/product/` | 商品卡片：圖、名稱、價格、收藏愛心 |
| Pagination | `component/common/` | 分頁（前後台共用） |

> 這頁包在前台外框裡（Header、Footer、ProfileGuard），外框另見 `StoreLayout.md`。
