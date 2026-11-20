package com.EEIT25.unicloth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebResourceConfig implements WebMvcConfigurer {
    // 後台上傳圖片的資料夾，從 application.yml 的 app.upload.product-image-dir 讀
    // （本機預設 src/main/resources/static/img/products，Railway 用 PRODUCT_IMAGE_DIR 指到 Volume）
    private final Path productImageDirectory;

    public WebResourceConfig(
            @Value("${app.upload.product-image-dir}") String productImageDir
    ) {
        this.productImageDirectory = Paths.get(productImageDir)
                .toAbsolutePath()
                .normalize();
    }

    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry
    ) {
        // 資料夾位置結尾一定要有 "/"，不然 Spring 會把它當成檔案而找不到裡面的圖
        String uploadLocation = productImageDirectory.toUri().toString();
        if (!uploadLocation.endsWith("/")) {
            uploadLocation = uploadLocation + "/";
        }

        // 先找上傳資料夾（後台新上傳的圖），找不到再找打包在 jar 裡的圖（爬蟲抓的原始商品圖）
        // （部署到 Railway 時容器裡沒有 src 資料夾，只有 app.jar，不加 classpath 這個位置，商品圖會全部 404）
        registry.addResourceHandler("/img/products/**")
                .addResourceLocations(
                        uploadLocation,
                        "classpath:/static/img/products/"
                );
    }
}
