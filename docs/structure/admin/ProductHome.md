# ProductHome 架構

`pages/admin/ProductHome.jsx`：商品部的「商品列表」功能頁。**沒有自己的網址**，是 `ProductDepartmentHome`（`/admin/product`）選到「商品列表」時顯示的內容

```
ProductHome
├─ ProductBatchActionBar
│   └─ ProductBatchCategoryModal
├─ DesktopProductTable
│   └─ StatusBadge
├─ MobileProductCards
│   └─ StatusBadge
├─ ProductCreateModal
│   └─ ProductStyleSelector
└─ ProductEditModal
    └─ ProductStyleSelector
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| ProductHome | `pages/admin/` | 頁面本體：搜尋（商品名稱或貨號）、狀態篩選、商品列表（每頁 20 筆，換頁按鈕寫在這個檔案裡，沒用共用的 Pagination）、單筆上架／下架、勾選商品、新增商品按鈕；功能列由 ProductDepartmentHome 傳進來（`tabNavigation`） |
| ProductBatchActionBar | `component/admin/product/` | 勾選商品後出現的批次操作列：整批上架、整批下架、移動分類、取消選取 |
| ProductBatchCategoryModal | `component/admin/product/` | 把勾選的商品移到另一個分類 |
| DesktopProductTable | 同檔案 | 桌面版商品表格，可勾選本頁商品 |
| MobileProductCards | 同檔案 | 手機版商品卡片 |
| StatusBadge | 同檔案 | 上架中／已下架的狀態標籤 |
| ProductCreateModal | `component/admin/product/` | 新增商品：名稱、6 位數貨號、分類、售價、原價（沒特價留空）、風格 |
| ProductEditModal | `component/admin/product/` | 編輯商品：基本資料、圖片（JPG／PNG／WEBP，5MB 內）、新增顏色與尺寸的 SKU、改庫存、刪除 SKU（要先下架） |
| ProductStyleSelector | `component/admin/product/` | 選商品風格標籤（新增、編輯共用） |
