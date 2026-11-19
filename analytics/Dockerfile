# 銷售分析服務（main.py）的容器，由 docker-compose.yml 的 analytics 服務建立、啟動
# 改了這個資料夾的程式要重建才會生效：docker compose up -d --build analytics

FROM python:3.13-slim-bookworm

# TZ：容器預設是 UTC，不設的話 date.today() 在台灣早上 8 點前會是前一天，「昨天」就算錯
# PYTHONUNBUFFERED：print / log 直接輸出，docker compose logs 才看得到
ENV TZ=Asia/Taipei \
    PYTHONUNBUFFERED=1

# tzdata：讓上面的 TZ 時區設定生效
# 連 PostgreSQL 用的 psycopg[binary] 已經內含驅動程式，不用另外裝系統套件
RUN apt-get update \
    && apt-get install -y --no-install-recommends tzdata \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# 先只複製 requirements.txt 裝套件：之後只改 .py 的話，這一步會沿用快取，重建很快
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY *.py .

CMD ["python", "main.py"]
