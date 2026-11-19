-- 商品風格標籤：由 DataSeeder 在匯入商品後執行（Flyway 執行時商品還沒匯入，所以不放在 migration）。
-- 每段都有 NOT EXISTS，重複執行不會產生重複資料。

-- 第一批經圖片與商品名稱確認的風格標籤。
INSERT INTO product_styles (
    product_id,
    style_id
)
SELECT
    product.id,
    style.id
FROM (
         VALUES
             ('women-488701', 'japanese'),
             ('women-488701', 'casual'),
             ('women-483890', 'natural'),
             ('women-483890', 'minimal'),
             ('women-484850', 'casual'),
             ('women-484850', 'preppy'),
             ('women-487286', 'sweet'),
             ('women-487286', 'casual'),
             ('women-487996', 'urban_work'),
             ('women-487996', 'minimal'),
             ('women-487855', 'minimal'),
             ('women-487855', 'urban_work'),
             ('men-487511', 'sporty'),
             ('men-487511', 'casual'),
             ('men-487488', 'minimal'),
             ('men-487488', 'urban_work'),
             ('men-486614', 'casual'),
             ('men-486614', 'urban_work'),
             ('men-486104', 'natural'),
             ('men-486104', 'casual'),
             ('kids-486666', 'preppy'),
             ('kids-486666', 'sweet'),
             ('kids-488239', 'sweet')
     ) AS tag_data(
                   product_slug,
                   style_code
    )
         JOIN products product
              ON product.slug = tag_data.product_slug
         JOIN styles style
              ON style.code = tag_data.style_code
WHERE NOT EXISTS (
    SELECT 1
    FROM product_styles existing_tag
    WHERE existing_tag.product_id = product.id
      AND existing_tag.style_id = style.id
);
-- 第二批經圖片與商品名稱確認的風格標籤。
INSERT INTO product_styles (
    product_id,
    style_id
)
SELECT
    product.id,
    style.id
FROM (
         VALUES
             ('men-492035', 'minimal'),
             ('men-492035', 'urban_work'),
             ('men-488758', 'minimal'),
             ('men-488758', 'urban_work'),
             ('men-486300', 'sporty'),
             ('men-486300', 'casual'),
             ('men-488801', 'japanese'),
             ('men-488801', 'natural'),
             ('women-488700', 'natural'),
             ('women-488700', 'casual'),
             ('women-487995', 'japanese'),
             ('women-487995', 'natural'),
             ('women-487001', 'sweet'),
             ('women-487001', 'minimal'),
             ('women-483281', 'sporty'),
             ('women-483281', 'minimal'),
             ('kids-486675', 'preppy'),
             ('kids-486675', 'casual'),
             ('kids-483406', 'sporty'),
             ('kids-483406', 'casual'),
             ('kids-486668', 'natural'),
             ('kids-486668', 'preppy'),
             ('kids-490470', 'urban_work'),
             ('kids-490470', 'casual')
     ) AS tag_data(
                   product_slug,
                   style_code
    )
         JOIN products product
              ON product.slug = tag_data.product_slug
         JOIN styles style
              ON style.code = tag_data.style_code
WHERE NOT EXISTS (
    SELECT 1
    FROM product_styles existing_tag
    WHERE existing_tag.product_id = product.id
      AND existing_tag.style_id = style.id
);
-- 依商品名稱與分類替剩餘商品批次計算風格標籤。
-- 用 TEMP TABLE（暫存表，連線結束就消失）放中間結果；比對用 ILIKE（不分大小寫），DRY、Polo 這類英文字大小寫不同也比對得到。
CREATE TEMP TABLE style_candidates (
                                   product_id BIGINT NOT NULL,
                                   style_code VARCHAR(50) NOT NULL,
                                   score INT NOT NULL
);

-- 每種風格依商品特徵給予分數。
INSERT INTO style_candidates (
    product_id,
    style_code,
    score
)
SELECT
    product.id,
    style_rule.style_code,
    style_rule.score
FROM products product
         JOIN categories category
              ON category.id = product.category_id
    CROSS JOIN LATERAL (
    VALUES (
        CONCAT(
            product.name,
            ' ',
            category.name,
            ' ',
            category.code
        )
    )
) AS product_data(search_text)
CROSS JOIN LATERAL (
    VALUES
        (
            'japanese',
            CASE
                WHEN product_data.search_text ILIKE '%繭型%'
                  OR product_data.search_text ILIKE '%寬版%'
                  OR product_data.search_text ILIKE '%寬鬆%'
                  OR product_data.search_text ILIKE '%九分%'
                  OR product_data.search_text ILIKE '%七分%'
                  OR product_data.search_text ILIKE '%亞麻%'
                  OR product_data.search_text ILIKE '%百褶%'
                  OR product_data.search_text ILIKE '%工裝%'
                  OR product_data.search_text ILIKE '%燈芯絨%'
                THEN 90
                ELSE 0
            END
        ),
        (
            'natural',
            CASE
                WHEN product_data.search_text ILIKE '%亞麻%'
                  OR product_data.search_text ILIKE '%棉麻%'
                  OR product_data.search_text ILIKE '%棉質%'
                  OR product_data.search_text ILIKE '%羊毛%'
                  OR product_data.search_text ILIKE '%喀什米爾%'
                  OR product_data.search_text ILIKE '%燈芯絨%'
                  OR product_data.search_text ILIKE '%刷毛%'
                  OR product_data.search_text ILIKE '%法蘭絨%'
                  OR product_data.search_text ILIKE '%柔軟%'
                  OR product_data.search_text ILIKE '%舒膚%'
                THEN 85
                ELSE 0
            END
        ),
        (
            'minimal',
            CASE
                WHEN product_data.search_text ILIKE '%圓領%'
                  OR product_data.search_text ILIKE '%V領%'
                  OR product_data.search_text ILIKE '%高領%'
                  OR product_data.search_text ILIKE '%針織%'
                  OR product_data.search_text ILIKE '%T恤%'
                  OR product_data.search_text ILIKE '%背心%'
                  OR product_data.search_text ILIKE '%內衣%'
                  OR product_data.search_text ILIKE '%牛仔褲%'
                  OR product_data.search_text ILIKE '%長褲%'
                  OR product_data.search_text ILIKE '%無袖%'
                  OR product_data.search_text ILIKE '%基本%'
                  OR product_data.search_text ILIKE '%簡約%'
                THEN 75
                ELSE 0
            END
        ),
        (
            'casual',
            CASE
                WHEN product_data.search_text ILIKE '%休閒%'
                  OR product_data.search_text ILIKE '%牛仔%'
                  OR product_data.search_text ILIKE '%連帽%'
                  OR product_data.search_text ILIKE '%外套%'
                  OR product_data.search_text ILIKE '%短褲%'
                  OR product_data.search_text ILIKE '%家居%'
                  OR product_data.search_text ILIKE '%帽子%'
                  OR product_data.search_text ILIKE '%襪%'
                  OR product_data.search_text ILIKE '%內褲%'
                THEN 70
                ELSE 20
            END
        ),
        (
            'sporty',
            CASE
                WHEN product_data.search_text ILIKE '%DRY%'
                  OR product_data.search_text ILIKE '%AIRism%'
                  OR product_data.search_text ILIKE '%運動%'
                  OR product_data.search_text ILIKE '%機能%'
                  OR product_data.search_text ILIKE '%防曬%'
                  OR product_data.search_text ILIKE '%防風%'
                  OR product_data.search_text ILIKE '%防潑水%'
                  OR product_data.search_text ILIKE '%彈性%'
                  OR product_data.search_text ILIKE '%束口%'
                  OR product_data.search_text ILIKE '%輕便%'
                  OR product_data.search_text ILIKE '%快乾%'
                  OR product_data.search_text ILIKE '%UV%'
                THEN 95
                ELSE 0
            END
        ),
        (
            'urban_work',
            CASE
                WHEN product_data.search_text ILIKE '%西裝%'
                  OR product_data.search_text ILIKE '%襯衫%'
                  OR product_data.search_text ILIKE '%POLO%'
                  OR product_data.search_text ILIKE '%風衣%'
                  OR product_data.search_text ILIKE '%大衣%'
                  OR product_data.search_text ILIKE '%開襟%'
                  OR product_data.search_text ILIKE '%針織衫%'
                  OR product_data.search_text ILIKE '%高領%'
                  OR product_data.search_text ILIKE '%長褲%'
                THEN 80
                ELSE 0
            END
        ),
        (
            'sweet',
            CASE
                WHEN product_data.search_text ILIKE '%洋裝%'
                  OR product_data.search_text ILIKE '%裙%'
                  OR product_data.search_text ILIKE '%蛋糕%'
                  OR product_data.search_text ILIKE '%荷葉%'
                  OR product_data.search_text ILIKE '%蝴蝶結%'
                  OR product_data.search_text ILIKE '%蕾絲%'
                  OR product_data.search_text ILIKE '%抓皺%'
                  OR product_data.search_text ILIKE '%芭蕾%'
                  OR product_data.search_text ILIKE '%短版%'
                THEN 88
                ELSE 0
            END
        ),
        (
            'preppy',
            CASE
                WHEN product_data.search_text ILIKE '%POLO%'
                  OR product_data.search_text ILIKE '%牛津%'
                  OR product_data.search_text ILIKE '%學院%'
                  OR product_data.search_text ILIKE '%格紋%'
                  OR product_data.search_text ILIKE '%百褶%'
                  OR product_data.search_text ILIKE '%西裝%'
                  OR product_data.search_text ILIKE '%背心%'
                  OR product_data.search_text ILIKE '%開襟%'
                  OR product_data.search_text ILIKE '%針織%'
                THEN 82
                ELSE 0
            END
        )
) AS style_rule(
    style_code,
    score
)
WHERE style_rule.score > 0
  AND NOT EXISTS (
    SELECT 1
    FROM product_styles existing_style
    WHERE existing_style.product_id = product.id
    );

-- 每件商品只取分數最高的兩個風格。
WITH ranked_candidates AS (
    SELECT
        candidate.product_id,
        candidate.style_code,
        ROW_NUMBER() OVER (
            PARTITION BY candidate.product_id
            ORDER BY candidate.score DESC, candidate.style_code
        ) AS style_rank
    FROM style_candidates candidate
)
INSERT INTO product_styles (
    product_id,
    style_id
)
SELECT
    candidate.product_id,
    style.id
FROM ranked_candidates candidate
         JOIN styles style
              ON style.code = candidate.style_code
WHERE candidate.style_rank <= 2
  AND NOT EXISTS (
    SELECT 1
    FROM product_styles existing_style
    WHERE existing_style.product_id = candidate.product_id
      AND existing_style.style_id = style.id
);

DROP TABLE style_candidates;