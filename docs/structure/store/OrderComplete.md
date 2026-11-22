# OrderComplete 架構

`pages/store/OrderComplete.jsx`（`/order-complete`）：訂單完成頁，顯示剛建立的訂單摘要

```
OrderComplete
└─ Row
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| OrderComplete | `pages/store/` | 頁面本體：顯示結帳頁帶過來的訂單摘要（下單時間、收件人、手機、配送、付款等）；直接輸入網址、沒有訂單資料就回首頁 |
| Row | 同檔案 | 一列：左邊項目名，右邊內容 |

> 這頁包在前台外框裡（Header、Footer、ProfileGuard），外框另見 `StoreLayout.md`。
