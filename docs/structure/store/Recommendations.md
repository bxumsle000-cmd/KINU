# Recommendations 架構

`pages/store/Recommendations.jsx`（`/recommendations`）：推薦商品頁，訪客看熱門商品，會員看個人化推薦

```
Recommendations
└─ ProductCard
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| Recommendations | `pages/store/` | 頁面本體：查 12 件推薦商品；會員另外抓收藏清單並顯示「推薦設定」入口，訪客顯示「登入取得個人推薦」，按愛心帶去登入頁 |
| ProductCard | `component/store/product/` | 商品卡片：圖、名稱、價格、收藏愛心 |

> 這頁包在前台外框裡（Header、Footer、ProfileGuard），外框另見 `StoreLayout.md`。
