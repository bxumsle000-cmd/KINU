# Checkout 架構

`pages/store/Checkout.jsx`（`/checkout`）：結帳，填收件、配送、付款方式，建立訂單後跳綠界付款

```
Checkout
├─ Field
└─ RadioGroup
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| Checkout | `pages/store/` | 頁面本體：訂購人／收件人（可同訂購人）、配送方式（宅配帶入會員地址，超商填門市）、付款方式。送出流程：驗證 → 建立訂單 → 信用卡／ATM 呼叫後端建立綠界交易，用表單 POST 跳轉綠界測試環境；貨到付款直接到訂單完成頁。防連點，訂單建立後不能重複送出 |
| Field | 同檔案 | 一個欄位：上面小標題，下面輸入框 |
| RadioGroup | 同檔案 | 一組單選按鈕（配送、付款方式） |

> 這頁包在前台外框裡（Header、Footer、ProfileGuard），外框另見 `StoreLayout.md`。
> 需要登入：`App.jsx` 在外面包了 `RequireLogin`。
