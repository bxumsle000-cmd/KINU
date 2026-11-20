package com.EEIT25.unicloth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebResourceConfig implements WebMvcConfigurer {
    private final Path productImageDirectory = Paths.get(
            "src",
            "main",
            "resources",
            "static",
            "img",
            "products"
    ).toAbsolutePath().normalize();

    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry
    ) {
        // 先找硬碟上的資料夾（本機開發、後台新上傳的圖），找不到再找打包在 jar 裡的圖
        // （部署到 Railway 時容器裡沒有 src 資料夾，只有 app.jar，不加 classpath 這個位置，商品圖會全部 404）
        registry.addResourceHandler("/img/products/**")
                .addResourceLocations(
                        productImageDirectory.toUri().toString(),
                        "classpath:/static/img/products/"
                );
    }
}