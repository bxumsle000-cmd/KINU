package com.EEIT25.unicloth.dto.store.category;

import com.EEIT25.unicloth.entity.Product;

/**
 * 商品列表上的一張卡片
 *
 * @param slug       商品字串 id
 * @param name       商品名稱
 * @param price      售價
 * @param origPrice  原價
 * @param newArrival 新品標籤
 * @param imageUrl   商品縮圖
 */
public record ProductCardResponse(
        String slug,
        String name,
        Integer price,
        Integer origPrice,
        boolean newArrival,
        String imageUrl
) {
    /**
     * 將商品轉成商品卡片回傳格式。
     */
    public static ProductCardResponse from(Product product) {
        return new ProductCardResponse(
                product.getSlug(),
                product.getName(),
                product.getPrice(),
                product.getOrigPrice(),
                product.isNewArrival(),
                product.getImageUrl());
    }
}
