package com.gen.todoservice.configs.cron;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${spring.application.version}")
    private String applicationVersion;

    @Value("${spring.application.description}")
    private String applicationDescription;

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public Docket createDocket() {

        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.gen"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(createApiInfo()).select().build();
    }

    private ApiInfo createApiInfo() {

        return new ApiInfo(applicationName, applicationDescription, applicationVersion, "",
                new Contact(
                        "Code Engine",
                        "https://www.codeengineroom.com.ng",
                        "info@codeengineroom.com.ng"),
                "Apache License Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0", Collections.emptyList());
    }

}
