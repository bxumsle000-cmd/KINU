# NewArrivals 架構

`pages/store/NewArrivals.jsx`（`/new-arrivals`）：新品頁，只顯示上架中且勾選新品標籤的商品

```
NewArrivals
├─ SortSelect
├─ ProductCard
└─ Pagination
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| NewArrivals | `pages/store/` | 頁面本體：查新品列表（預設由新到舊，可切換價格排序）；會員才抓收藏清單，訪客按愛心帶去登入頁 |
| SortSelect | `component/store/product/` | 排序下拉選單 |
| ProductCard | `component/store/product/` | 商品卡片：圖、名稱、價格（特價顯示原價刪除線）、收藏愛心 |
| Pagination | `component/common/` | 分頁（前後台共用） |

> 這頁包在前台外框裡（Header、Footer、ProfileGuard），外框另見 `StoreLayout.md`。
