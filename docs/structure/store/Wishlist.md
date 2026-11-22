# Wishlist 架構

`pages/store/Wishlist.jsx`（`/wishlist`）：我的收藏

```
Wishlist
└─ WishListCard
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| Wishlist | `pages/store/` | 頁面本體：查收藏清單；取消收藏成功後從畫面拿掉並通知 Header；沒有收藏時顯示「去逛逛」 |
| WishListCard | `component/store/` | 收藏卡片，按愛心取消收藏 |

> 這頁包在前台外框裡（Header、Footer、ProfileGuard），外框另見 `StoreLayout.md`。
> 需要登入：`App.jsx` 在外面包了 `RequireLogin`。
