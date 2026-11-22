# MemberProfile 架構

`pages/store/MemberProfile.jsx`（`/member`，會員中心預設子頁面）：修改會員資料

```
MemberLayout
└─ MemberProfile
    ├─ Field
    └─ ReadOnlyField
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| MemberProfile | `pages/store/` | 頁面本體：只能改姓名、手機、地址；Email、性別、生日只顯示不能改 |
| Field | 同檔案 | 可編輯的欄位：上面小標題，下面輸入框 |
| ReadOnlyField | 同檔案 | 只能看、不能改的欄位 |

> 外框是 `MemberLayout`（另見 `MemberLayout.md`），再外面是前台外框 `StoreLayout.md`。
> 需要登入：`App.jsx` 在 `/member` 外面包了 `RequireLogin`。
