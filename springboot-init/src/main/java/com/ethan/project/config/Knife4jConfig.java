package com.ethan.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description: Knife4j 接口文档配置
 *                 https://doc.xiaominfo.com/knife4j/documentation/get_start.html
 * @Auther: http://www.0808.icu
 */
@Configuration
@EnableSwagger2
@Profile("dev")
public class Knife4jConfig {
    public Docket defaultApi2(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("project-backend")
                        .description("project-backend")
                        .version("1.0")
                        .build())
                .select()
                // 指定 Controller 扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.eh.project.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
