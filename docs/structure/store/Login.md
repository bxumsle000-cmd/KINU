# Login 架構

`pages/store/Login.jsx`（`/login`）：會員登入（Google）

```
Login
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| Login | `pages/store/` | 頁面本體：Firebase 跳出 Google 登入 → 把 ID token 送後端換 JWT → 整頁重新載入（第一次登入先去補填資料）；使用者自己關掉 Google 視窗不算錯誤 |

> 這頁包在前台外框裡（Header、Footer、ProfileGuard），外框另見 `StoreLayout.md`。
