"""
連 PostgreSQL（跟 Spring Boot 同一個資料庫）。

連線資訊預設跟 application.yml 一樣（本機 docker 的 15432）；要換就設環境變數 DB_HOST、DB_PORT、DB_NAME、DB_USER、DB_PASSWORD。
SQL 裡的參數用 %s 佔位（psycopg 的寫法），不是 ?。
"""

import os

import pandas as pd
import psycopg


def _connection_info() -> dict:
    return {
        "host": os.getenv("DB_HOST", "localhost"),
        "port": os.getenv("DB_PORT", "15432"),
        "dbname": os.getenv("DB_NAME", "unicloth"),
        "user": os.getenv("DB_USER", "unicloth"),
        "password": os.getenv("DB_PASSWORD", "Eeit25@Unicloth"),
    }


def query(sql: str, params: tuple = ()) -> pd.DataFrame:
    """執行 SELECT，結果轉成 DataFrame（欄位名稱就是 SQL 裡 AS 取的名字）"""
    with psycopg.connect(**_connection_info()) as conn:
        with conn.cursor() as cursor:
            cursor.execute(sql, params)
            columns = [col.name for col in cursor.description]
            return pd.DataFrame.from_records(cursor.fetchall(), columns=columns)
