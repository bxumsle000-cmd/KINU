# ProductDepartmentHome 架構

`pages/admin/ProductDepartmentHome.jsx`（`/admin/product`）：商品部首頁，右上角功能列切換六個功能頁

```
ProductDepartmentHome
├─ AdminHeader
│   └─ ChangePasswordModal
│       └─ Modal
├─ ProductTabNavigation
├─ ProductHome（商品列表，另見 ProductHome.md）
└─ ProductPanelLayout
    ├─ ProductAnalyticsPanel（銷售分析）
    ├─ ProductInventoryPanel（進貨入庫）
    ├─ ProductStockWarningPanel（庫存警示）
    │   ├─ ProductImage
    │   ├─ DesktopWarningTable
    │   │   └─ VariantList
    │   └─ MobileWarningCards
    │       └─ VariantList
    ├─ ProductSchedulePanel（預定下架）
    └─ ProductCategoryPanel（分類管理）
        ├─ CategoryNode
        └─ CreateCategoryModal
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| ProductDepartmentHome | `pages/admin/` | 頁面本體：頂部「KINU ADMIN 商品部」標題列，記住目前在哪個功能頁；從庫存警示按「去入庫」會帶著商品 id 切到進貨入庫 |
| AdminHeader | `component/admin/shared/` | 頂部列：顯示目前登入的人，設定選單（更改密碼、登出） |
| ChangePasswordModal | `component/admin/shared/` | 更改自己密碼的視窗 |
| Modal | `component/admin/shared/` | 共用的視窗外框，點背景關閉 |
| ProductTabNavigation | 同檔案 | 功能列：商品列表、銷售分析、進貨入庫、庫存警示、預定下架、分類管理；手機可橫向滑動 |
| ProductPanelLayout | 同檔案 | 商品列表以外的功能頁共用版面，功能列固定在內容區右上角 |
| ProductHome | `pages/admin/` | 商品列表（內容多，另寫一份 `ProductHome.md`） |
| ProductAnalyticsPanel | `component/admin/product/` | 銷售分析：期間（最近 7／30／90 天）、商品銷售排行，顯示本期件數、銷售額、上期件數、差異和趨勢（新進榜、持平等） |
| ProductInventoryPanel | `component/admin/product/` | 進貨入庫：搜尋商品、填各 SKU 進貨數量與進貨單號送出入庫，也能盤點調整；下面有異動紀錄（每頁 10 筆） |
| ProductStockWarningPanel | `component/admin/product/` | 庫存警示：可設定「快賣完」的庫存標準，列出上架中商品缺貨或快賣完的款式，每列可按「去入庫」 |
| ProductImage | `ProductStockWarningPanel.jsx` 同檔案 | 商品小圖（把資料庫的相對路徑轉成網站路徑） |
| DesktopWarningTable / MobileWarningCards | `ProductStockWarningPanel.jsx` 同檔案 | 桌面版表格／手機版卡片 |
| VariantList | `ProductStockWarningPanel.jsx` 同檔案 | 一件商品底下庫存不足的款式清單 |
| ProductSchedulePanel | `component/admin/product/` | 預定下架：搜尋上架中的商品，設定或清除預定下架時間 |
| ProductCategoryPanel | `component/admin/product/` | 分類管理：多層分類樹，在某個分類底下新增子分類（商品只能放在最底層分類） |
| CategoryNode | `ProductCategoryPanel.jsx` 同檔案 | 分類樹的一個節點（底下還有子分類就往下畫） |
| CreateCategoryModal | `ProductCategoryPanel.jsx` 同檔案 | 新增分類視窗：分類代碼（小寫英文、數字、底線、連字號）、名稱 |
