-- ============================================================================
-- 移除推薦系統、商品風格、HOT 標籤
-- - member_product_views：推薦系統用的會員商品瀏覽紀錄
-- - styles / product_styles / style_aliases / style_relations：商品風格標籤與風格搜尋
-- - products.is_hot：HOT 標籤
-- 先刪有 FK 指向別人的子表，最後刪 styles；索引會跟著資料表一起刪掉。
-- ============================================================================

DROP TABLE IF EXISTS member_product_views;

DROP TABLE IF EXISTS style_relations;
DROP TABLE IF EXISTS style_aliases;
DROP TABLE IF EXISTS product_styles;
DROP TABLE IF EXISTS styles;

DROP INDEX IF EXISTS idx_products_hot;
ALTER TABLE products DROP COLUMN IF EXISTS is_hot;
