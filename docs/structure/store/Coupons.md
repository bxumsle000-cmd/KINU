# Coupons 架構

`pages/store/Coupons.jsx`（`/coupons`）：我的折價券

```
Coupons
└─ CouponCard
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| Coupons | `pages/store/` | 頁面本體：分頁籤（可使用／已使用／已過期，顯示各幾張）、折價券列表 |
| CouponCard | `component/store/` | 折價券卡片（PERCENT 顯示幾折，AMOUNT 顯示折多少元） |

> 這頁包在前台外框裡（Header、Footer、ProfileGuard），外框另見 `StoreLayout.md`。
> 需要登入：`App.jsx` 在外面包了 `RequireLogin`。
