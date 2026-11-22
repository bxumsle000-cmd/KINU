# Orders 架構

`pages/store/Orders.jsx`（`/orders`）：我的訂單，依狀態篩選

```
Orders
└─ Row
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| Orders | `pages/store/` | 頁面本體：狀態分頁籤（顯示每個狀態幾筆）、訂單卡片（編號、下單時間、狀態、商品明細、收件與配送、付款、備註） |
| Row | 同檔案 | 一列：左邊項目名，右邊內容 |

> 這頁包在前台外框裡（Header、Footer、ProfileGuard），外框另見 `StoreLayout.md`。
> 需要登入：`App.jsx` 在外面包了 `RequireLogin`。
