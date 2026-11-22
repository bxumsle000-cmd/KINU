# StoreLayout 架構

`App.jsx` 裡的 `StoreLayout`（`/admin` 以外的所有網址）：前台共用外框，上面 Header、下面 Footer，中間依網址換頁

```
StoreLayout
└─ CountProvider
    ├─ Header
    │   ├─ MegaMenu
    │   ├─ MobileMenu
    │   └─ HeaderActions
    │       ├─ LanguageMenu
    │       └─ MemberMenu
    ├─ ProfileGuard
    ├─ （依網址顯示的頁面；部分頁面外面包 RequireLogin）
    └─ Footer
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| StoreLayout | `App.jsx` 同檔案 | 前台外框 + 前台所有路由 |
| CountProvider | `js/` | 保管 Header 上收藏、購物車的數字，各頁面加入收藏／購物車後通知 Header 更新（只包前台） |
| Header | `component/store/layout/` | 頂部列：抓一次 `/api/categories` 整棵分類樹，控制哪個分類選單展開、手機版選單開關，點外面就收起 |
| MegaMenu | `component/store/layout/` | 桌面版分類下拉選單（女裝、男裝、兒童底下的分類） |
| MobileMenu | `component/store/layout/` | 手機版分類選單：上排新品、女裝、男裝、兒童、推薦，下面列所選分類的第 2 層 |
| HeaderActions | `component/store/layout/` | Header 右邊：搜尋框（關鍵字放網址）、收藏和購物車數字、語言、會員中心 |
| LanguageMenu | `component/store/layout/` | 語言選單，點地球打開 |
| MemberMenu | `component/store/layout/` | 會員中心選單，點「會員中心」打開 |
| ProfileGuard | `component/guard/` | 已登入但還沒補填資料又離開 `/complete-profile`，就直接登出 |
| RequireLogin | `component/guard/` | 要登入的頁面外面包這個：沒登入只顯示提示，不畫頁面（避免打 API 被回 401） |
| Footer | `component/store/layout/` | 頁尾 |
