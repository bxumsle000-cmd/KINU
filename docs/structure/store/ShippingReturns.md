# ShippingReturns 架構

`pages/store/ShippingReturns.jsx`（`/shipping-returns`）：配送與退貨說明（純文字，訪客也能看）

```
ShippingReturns
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| ShippingReturns | `pages/store/` | 頁面本體：運費門檻、配送與付款方式、退貨說明；內容要跟後端 `CheckoutService` 一致 |

> 這頁包在前台外框裡（Header、Footer、ProfileGuard），外框另見 `StoreLayout.md`。
