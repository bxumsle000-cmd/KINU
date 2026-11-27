# ProductDetail 架構

`pages/store/ProductDetail.jsx`（`/product/:slug`）：商品詳情頁，選顏色、尺寸、數量後加入購物車

```
ProductDetail
└─ Accordion
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| ProductDetail | `pages/store/` | 頁面本體：商品大圖（跟著顏色換）、收藏愛心、價格（特價時售價變紅）、選顏色／尺寸（缺貨反灰）／數量（上限是庫存）、加入購物車；沒登入按加入購物車會帶去登入頁 |
| Accordion | `component/store/product/` | 可摺疊區塊：商品說明、退換貨說明、運費說明 |

> 這頁包在前台外框裡（Header、Footer、ProfileGuard），外框另見 `StoreLayout.md`。
