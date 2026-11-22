# OrderHome 架構

`pages/admin/OrderHome.jsx`（`/admin/order`）：訂單物流人員的訂單查詢與出貨、送達處理

```
OrderHome
├─ AdminHeader
│   └─ ChangePasswordModal
│       └─ Modal
├─ Pagination
└─ OrderDetailModal
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| OrderHome | `pages/admin/` | 頁面本體：篩選列（關鍵字：訂單編號、會員 Email、收件人；訂單狀態；配送方式）、訂單列表（編號／時間、會員、收件人、配送、付款、金額、狀態）；點「詳細」開詳細視窗 |
| AdminHeader | `component/admin/shared/` | 頂部列：顯示目前登入的人，設定選單（更改密碼、登出） |
| ChangePasswordModal | `component/admin/shared/` | 更改自己密碼的視窗 |
| Modal | `component/admin/shared/` | 共用的視窗外框，點背景關閉 |
| Pagination | `component/common/` | 分頁（前後台共用） |
| OrderDetailModal | `component/admin/order/` | 訂單詳細視窗：訂單狀態、收件與物流、商品明細、付款與金額（小計、折價券折抵、運費、實付）；可填物流商、追蹤碼「確認出貨」，再「確認送達」／「確認取貨」；更新後只替換列表裡那一筆 |
