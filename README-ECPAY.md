# 綠界金流測試教學

## 一、功能介紹

付款流程：

1. 顧客在結帳頁選擇信用卡（或 ATM）付款。
2. Spring Boot 建立訂單（狀態 `UNPAID` 未付款）與付款紀錄。
3. 網頁跳轉到綠界官方測試付款頁面，完成付款。
4. 綠界透過 HTTPS 通知後端。
5. 後端驗證通知後更新資料庫：`payments.status` 改成 `PAID`，訂單記錄付款時間，訂單狀態變成 `PENDING`（訂單確認中）。

沒付款的訂單會自動取消，並還原庫存與折價券（見「六、訂單狀態與自動取消」）。

目前是**綠界測試環境，不是正式金流**，不會真的扣款。

---

## 二、本機的付款通知怎麼送進來

綠界的伺服器在網路上，連不到你電腦的 `localhost`，所以要靠兩個東西：

- **tunnel 容器**（`docker-compose.yml` 的 `unicloth-tunnel`，Cloudflare Quick Tunnel）：產生一個 `https://xxxx.trycloudflare.com` 網址給綠界用。不用另外安裝 cloudflared。
- **`ecpay-callback-proxy.cjs`**：在本機 `127.0.0.1:8787` 接收通知，再轉給 Spring Boot 的 `8080`。只允許 `/api/payments/ecpay/notify` 這個路徑，其他 API 從外面都連不到。

整條路徑：

```text
綠界 → Cloudflare 網址 → tunnel 容器（Docker）→ 8787（Node Proxy）→ 8080（Spring Boot）
```

### 網址每次都會變，要自己設定嗎？

**不用。** tunnel 每次重新啟動網址都會變，但 Spring Boot 每次建立付款時，都會自動去問 tunnel 目前的網址（`EcpayReturnUrlResolver`）。

想自己看目前的網址：

```bash
docker compose logs tunnel
```

> Railway 線上版不需要 tunnel 和 proxy：已經用環境變數 `ECPAY_RETURN_URL` 設定固定的付款通知網址。

---

## 三、本機啟動步驟

1. 打開 Docker Desktop，確認 `unicloth-tunnel` 容器有在跑（第一次要先 `docker compose up -d`）。
2. 另外開一個終端機，啟動付款通知 Proxy（需要 Node.js），**視窗不要關**：

   ```bash
   node ecpay-callback-proxy.cjs
   ```

   成功會顯示「綠界付款通知專用入口已啟動」。

3. 啟動 Spring Boot（見 [README.md](README.md)）。

**不需要設定 `ECPAY_RETURN_URL`**，後端會自動抓 tunnel 的網址。如果你以前在同一個視窗設定過，它會蓋掉自動抓的網址（而且舊網址早就失效），請開新視窗或先清掉：

- CMD：`set ECPAY_RETURN_URL=`
- PowerShell：`Remove-Item Env:ECPAY_RETURN_URL`

建立付款時，後端 log 會印出目前用的網址：

```text
ECPay ReturnURL (from tunnel): https://xxxx.trycloudflare.com/api/payments/ecpay/notify
```

如果看到 `falls back to localhost`，代表 tunnel 沒開。