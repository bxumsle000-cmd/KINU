# RecommendationSettings 架構

`pages/store/RecommendationSettings.jsx`（`/member/recommendation-settings`）：推薦設定，清除瀏覽紀錄

```
MemberLayout
└─ RecommendationSettings
```

## Component 作用

| Component | 位置 | 作用 |
|---|---|---|
| RecommendationSettings | `pages/store/` | 頁面本體：說明推薦是依瀏覽紀錄分析風格偏好；按「清除瀏覽紀錄」確認後清除，顯示清了幾筆，之後的推薦重新累積 |

> 外框是 `MemberLayout`（另見 `MemberLayout.md`），再外面是前台外框 `StoreLayout.md`。
> 需要登入：`App.jsx` 在 `/member` 外面包了 `RequireLogin`。
