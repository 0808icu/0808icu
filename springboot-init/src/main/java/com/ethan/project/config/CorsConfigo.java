package com.ethan.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description:全局跨域配置
 * @Auther: http://www.0808.icu
 */
@Configuration
public class CorsConfigo implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry){
        //覆盖所有请求
        corsRegistry.addMapping("/**")
            //允许发送Cookie
            .allowCredentials(true)
            //放行哪些域名，必须要用patterns，否则 * 会和allowCredentials冲突
            .allowedOriginPatterns("*")
            .allowedMethods("PUT","DELETE","GET","POST","OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders("*");

    }
}
