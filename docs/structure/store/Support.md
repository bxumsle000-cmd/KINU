# Support 架構

`pages/store/Support.jsx`（`/support`）：客服中心，建立客服單、看自己的客服單和回覆

```
Support
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| Support | `pages/store/` | 頁面本體：建立客服單表單（問題類型、聯絡 Email、內容），送出後顯示客服單號並直接加進下方清單；「我的客服單」列出自己的案件、客服回覆和狀態（後端依 token 只回自己的） |

> 這頁包在前台外框裡（Header、Footer、ProfileGuard），外框另見 `StoreLayout.md`。
> 需要登入：`App.jsx` 在外面包了 `RequireLogin`。
