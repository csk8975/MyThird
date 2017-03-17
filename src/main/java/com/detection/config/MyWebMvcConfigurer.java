package com.detection.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @fileName MyWebMvcConfigurer.java
 * @author csk
 * @createTime 2017年3月8日 下午2:52:54
 * @version 1.0
 * @function
 */
/*@Configuration
public class MyWebMvcConfigurer extends WebMvcConfigurerAdapter {
    
    @Value("${downloadPath}")
    private String downloadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(downloadPath+"**").addResourceLocations("classpath:/"+downloadPath);
        super.addResourceHandlers(registry);
    }
}*/
