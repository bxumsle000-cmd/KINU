# Kinu

Kinu 是一個服飾電商網站，分成給顧客購物的**前台商店**，和給員工管理的**後台系統**。後台依部門分權限，不同部門登入後看到的功能不一樣。

## 線上網址（Railway）

| 頁面 | 網址 |
|------|------|
| 前台商店 | <https://kinu-production-d1559cff.up.railway.app/> |
| 後台管理 | <https://kinu-production-d1559cff.up.railway.app/admin/login> |

### 後台員工測試帳號

密碼全部都是 **`Test1234`**。

| 帳號 | 部門 |
|------|------|
| `sys01` | 系統管理（可進所有部門） |
| `product01` | 商品部 |
| `order01` | 訂單物流 |
| `cs01` | 客服 |
| `mkt01` | 行銷 |

> 這些帳號寫在 `src/main/resources/db/migration/V1__init.sql`，資料庫第一次建立時會自動加入。

## 功能

### 前台商店

- 會員用 Google 帳號登入（Firebase）
- 商品瀏覽、分類、搜尋、新品、風格推薦
- 收藏清單、購物車、優惠券、結帳
- 付款：貨到付款、綠界信用卡 / ATM（測試環境，不會真的扣款）
- 訂單查詢、客服單

### 後台管理

登入時要選部門，每個部門看到的功能不同：

| 部門 | 負責的功能 |
|------|-----------|
| 商品部 | 商品、分類、上下架排程、庫存、商品分析 |
| 訂單物流 | 訂單處理、出貨 |
| 客服 | 客服單 |
| 行銷 | 優惠券、會員、銷售數據 |
| 系統管理 | 員工帳號管理，並可進入所有部門 |

## 使用技術

| 部分 | 技術 |
|------|------|
| 後端 | Java 21、Spring Boot、Spring Security + JWT、JPA、Flyway |
| 資料庫 | PostgreSQL |
| 前端 | React + Vite（已 build 好放在 `src/main/resources/static/`） |
| 銷售分析 | Python 服務（`analytics/`） |
| 金流 | 綠界 ECPay 測試環境 |
| 部署 | Railway（Docker） |

## Railway 上的架構

Railway 專案 `responsible-intuition` 裡有三個服務，都從 GitHub 的 `main` 分支自動部署：

| 服務 | 內容 |
|------|------|
| `KINU` | Spring Boot（含前端），用根目錄的 `Dockerfile` 打包；掛 Volume `/data` 存後台上傳的商品圖片 |
| `Postgres` | 資料庫，掛 Volume 保存資料 |
| `analytics` | Python 銷售分析服務，只在 Railway 內部網路給 `KINU` 呼叫，沒有對外網址 |

`KINU` 服務用環境變數覆蓋 `application.yml` 的本機預設值：`DB_URL`、`DB_USERNAME`、`DB_PASSWORD`、`JWT_SECRET`、`PRODUCT_IMAGE_DIR`、`ANALYTICS_BASE_URL`、`ECPAY_RETURN_URL`、`ECPAY_CLIENT_BACK_URL`、`SHOW_SQL`、`SWAGGER_ENABLED`。

---

# 本機開發

## 第一次：建立 Docker 容器

**先打開 Docker 應用程式**，再在專案資料夾執行：

```bash
docker compose up -d
```

會啟動三個容器：

- `unicloth-postgres`：PostgreSQL 資料庫
- `unicloth-tunnel`：綠界付款通知用的對外網址
- `unicloth-analytics`：行銷後台「銷售數據」用的分析服務（第一次要建映像檔，要等幾分鐘）

> 之後只要打開 Docker 應用程式，容器就會自動啟動，不用再跑這行。
> 想清空資料庫整組重來：先 `docker compose down -v` 再 `docker compose up -d`（**會刪掉所有資料**）。

## 每次：啟動 Spring Boot

確認 Docker 有開著（資料庫沒起來，Spring Boot 會啟動失敗），在專案資料夾執行：

```bash
# Windows PowerShell / cmd
.\mvnw.cmd spring-boot:run

# Git Bash / macOS / Linux
./mvnw spring-boot:run
```

啟動完成後開：

- 前台：<http://localhost:8080/>
- 後台：<http://localhost:8080/admin/login>
- API 文件（Swagger UI）：<http://localhost:8080/swagger-ui/index.html>

第一次啟動時會自動匯入商品（`data/products.json`）和展示用假資料（會員、訂單、優惠券、客服單…），已經匯過就會跳過。開關在 `application.yml` 的 `app.seed`。

前端已經 build 好，**不用另外啟動前端**。要修改前端請看 [README-FRONTEND.md](README-FRONTEND.md)。

## 要測綠界付款時

另外開一個終端機，在專案資料夾執行（需要先安裝 Node.js），視窗不要關：

```bash
node ecpay-callback-proxy.cjs
```

完整說明請看 [README-ECPAY.md](README-ECPAY.md)。只用貨到付款的話不需要這一步。

## 資料庫連線（DBeaver / pgAdmin）

| 欄位 | 值 |
|------|----|
| Host | `localhost` |
| Port | `15432` |
| Database | `unicloth` |
| Username | `unicloth` |
| 密碼 | `Eeit25@Unicloth` |

> Spring Boot 要至少成功啟動過一次，才會建出資料表。

## 本機試跑 Docker 映像檔

跟 Railway 用同一個 `Dockerfile`：

```bash
docker build -t kinu-app .
docker run --rm -p 8080:8080 -e DB_URL="jdbc:postgresql://host.docker.internal:15432/unicloth" kinu-app
```