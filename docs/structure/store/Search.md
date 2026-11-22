# Search 架構

`pages/store/Search.jsx`（`/search?keyword=...`）：關鍵字搜尋結果

```
Search
├─ SortSelect
├─ ProductCard
└─ Pagination
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| Search | `pages/store/` | 頁面本體：從網址讀關鍵字查商品，換關鍵字回第 0 頁；條件連續變動時只用最後一次請求的結果 |
| SortSelect | `component/store/product/` | 排序下拉選單 |
| ProductCard | `component/store/product/` | 商品卡片：圖、名稱、價格、收藏愛心 |
| Pagination | `component/common/` | 分頁（前後台共用） |

> 這頁包在前台外框裡（Header、Footer、ProfileGuard），外框另見 `StoreLayout.md`。
