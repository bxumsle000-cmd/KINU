package com.EEIT25.unicloth.service.shop;

import com.EEIT25.unicloth.dto.store.category.ProductCardResponse;
import com.EEIT25.unicloth.dto.store.product.ProductDetailResponse;
import com.EEIT25.unicloth.entity.Product;
import com.EEIT25.unicloth.entity.ProductVariant;
import com.EEIT25.unicloth.enums.product.ProductStatus;
import com.EEIT25.unicloth.exception.ApiException;
import com.EEIT25.unicloth.repository.product.ProductRepository;
import com.EEIT25.unicloth.repository.product.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 商品相關服務<br>
 * - {@link #getProductDetail(String)}：商品詳細頁<br>
 * - {@link #getNewArrivals(Pageable)}：查詢目前上架的新品<br>
 * - {@link #getProductsBySearch(String, Pageable)}：用商品名稱搜尋
 */
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;

    /**
     * 商品詳細頁（只查上架中的）：
     * 把所有 SKU 依顏色分組，每個顏色底下帶各尺寸。
     */
    @Transactional(readOnly = true)
    public ProductDetailResponse getProductDetail(
            String slug
    ) {
        Product product =
                productRepository.findOnSaleBySlug(slug)
                        .orElseThrow(() ->
                                ApiException.notFound(
                                        "找不到商品：" + slug
                                )
                        );

        List<ProductVariant> variants =
                productVariantRepository
                        .findByProductIdOrderByIdAsc(
                                product.getId()
                        );

        return ProductDetailResponse.from(
                product,
                variants
        );
    }

    /**
     * 查詢目前上架且標示為新品的商品。
     */
    @Transactional(readOnly = true)
    public Page<ProductCardResponse> getNewArrivals(
            Pageable pageable
    ) {
        return productRepository
                .findByStatusAndNewArrivalTrue(
                        ProductStatus.ON_SALE,
                        pageable
                )
                .map(ProductCardResponse::from);
    }

    /**
     * 用商品名稱搜尋上架中的商品（不分大小寫、分頁）。
     * 關鍵字是空的就回空頁，不會撈出全部商品。
     */
    @Transactional(readOnly = true)
    public Page<ProductCardResponse> getProductsBySearch(
            String keyword,
            Pageable pageable
    ) {
        String normalizedKeyword =
                keyword == null
                        ? ""
                        : keyword.trim();

        // 空白關鍵字不查詢全部商品。
        if (normalizedKeyword.isEmpty()) {
            return Page.empty(pageable);
        }

        return productRepository
                .findByStatusAndNameContainingIgnoreCase(
                        ProductStatus.ON_SALE,
                        normalizedKeyword,
                        pageable
                )
                .map(ProductCardResponse::from);
    }
}
