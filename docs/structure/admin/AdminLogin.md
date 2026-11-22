# AdminLogin 架構

`pages/admin/AdminLogin.jsx`（`/admin/login`）：員工登入頁，選部門 + 帳號密碼，登入成功後進該部門首頁

```
AdminLogin
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| AdminLogin | `pages/admin/` | 頁面本體：選擇部門、輸入帳號密碼送出；成功後存 token、姓名、部門、帳號，依部門跳到 `/admin/product`、`/admin/order` 等；登入中鎖住按鈕避免連按；開發模式（`npm run dev`）會在畫面上顯示測試帳號 |

> 沒有用到其他 component。
