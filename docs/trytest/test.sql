SELECT p.name,
       pv.color,
       pv.size,
       pv.sku_code,
       pv.stock
FROM products as p
JOIN product_variants as pv
ON p.id = pv.product_id
WHERE p.status = 'ON_SALE'
  AND pv.stock < 5
ORDER BY pv.stock DESC;


