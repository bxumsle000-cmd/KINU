# CsHome 架構

`pages/admin/CsHome.jsx`（`/admin/cs`）：客服人員的客服單查詢、回覆與結案

```
CsHome
├─ AdminHeader
│   └─ ChangePasswordModal
│       └─ Modal
├─ Pagination
└─ CsTicketDetailModal
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| CsHome | `pages/admin/` | 頁面本體：篩選列（關鍵字：客服單號、Email、問題類型、問題內容；狀態）、客服單列表；桌面版用表格，螢幕不到 760px 改用卡片；點「詳細」開詳細視窗 |
| AdminHeader | `component/admin/shared/` | 頂部列：顯示目前登入的人，設定選單（更改密碼、登出） |
| ChangePasswordModal | `component/admin/shared/` | 更改自己密碼的視窗 |
| Modal | `component/admin/shared/` | 共用的視窗外框，點背景關閉 |
| Pagination | `component/common/` | 分頁（前後台共用） |
| CsTicketDetailModal | `component/admin/cs/` | 客服單詳細視窗：案件資訊、會員問題全文、客服回覆輸入框；可「儲存回覆」或「儲存回覆並結案」，存好後只替換列表裡那一筆，不重查整頁 |
