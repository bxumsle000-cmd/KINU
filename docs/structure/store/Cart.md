# Cart 架構

`pages/store/Cart.jsx`（`/cart`）：購物車，改數量、移除、選折價券後前往結帳

```
Cart
└─ CartItem
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| Cart | `pages/store/` | 頁面本體：商品清單、訂單摘要（小計只算上架中商品、免運進度條、折價券、合計）；小計變了就重抓可用折價券，沒達最低消費的券列出但不能選；「前往結帳」把選的券帶到結帳頁 |
| CartItem | `component/store/` | 購物車的一筆：改數量、移除；下架商品不連到商品頁 |

> 這頁包在前台外框裡（Header、Footer、ProfileGuard），外框另見 `StoreLayout.md`。
> 需要登入：`App.jsx` 在外面包了 `RequireLogin`。
