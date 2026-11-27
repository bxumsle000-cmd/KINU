# MemberLayout 架構

`pages/store/MemberLayout.jsx`（`/member` 開頭的網址）：會員中心外框，左邊選單，右邊放子頁面

```
MemberLayout
└─ <Outlet/>
    └─ MemberProfile（/member）
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| MemberLayout | `pages/store/` | 會員中心外框：「會員中心」大標題、左邊選單（會員資料），右邊用 `<Outlet/>` 放目前網址的子頁面；子頁面在 `App.jsx` 的 `/member` 底下設定 |
| MemberProfile | `pages/store/` | 會員資料子頁面，另見 `MemberProfile.md` |

> 這頁包在前台外框裡（Header、Footer、ProfileGuard），外框另見 `StoreLayout.md`。
> 需要登入：`App.jsx` 在外面包了 `RequireLogin`。
