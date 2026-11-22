# CompleteProfile 架構

`pages/store/CompleteProfile.jsx`（`/complete-profile`）：第一次用 Google 登入後補填個人資料，填完才能用購物車、結帳等功能

```
CompleteProfile
└─ Field
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| CompleteProfile | `pages/store/` | 頁面本體：姓名（先帶 Google 名字）、手機、性別、生日、地址（選填）；送出後整頁重新載入，讓路由守衛重新確認已補填 |
| Field | 同檔案 | 一個欄位：上面小標題，下面輸入框 |

> 這頁包在前台外框裡（Header、Footer、ProfileGuard），外框另見 `StoreLayout.md`。
