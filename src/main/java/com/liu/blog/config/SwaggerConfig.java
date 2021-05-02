package com.liu.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket myDocket(Environment environment){
        Profiles profiles = Profiles.of("dev");
        boolean isEnable = environment.acceptsProfiles(profiles);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(isEnable)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.liu.blog.controller"))
                .build();
    }

    public ApiInfo apiInfo(){
        Contact contact = new Contact("Kevin", "Liu", "1045675355@qq.com");

        return new ApiInfo(
                "博客Swagger文档",
                "Api接口文档",
                "1.0",
                //TODO Remote URL
                "localhost:8080",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList()
        );
    }
}
