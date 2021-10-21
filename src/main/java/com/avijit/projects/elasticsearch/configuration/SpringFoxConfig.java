package com.avijit.projects.elasticsearch.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;

@Configuration
public class SpringFoxConfig {

    @Bean
    public Docket api() {
        ApiInfo apiInfo = (new ApiInfoBuilder())
                .title("User Suport Tickets Search")
                .description("An API that can search user support tickets")
                .version("1.0.0")
                .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.avijit.projects.elasticsearch"))
                .paths(PathSelectors.ant("/v1/**"))
                .build()
                .apiInfo(apiInfo);
    }




    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .displayRequestDuration(true)
                .validatorUrl("")
                .deepLinking(true)
                .displayOperationId(true)
                .defaultModelsExpandDepth(1)
                .defaultModelExpandDepth(1)
                .docExpansion(DocExpansion.LIST)
                .operationsSorter(OperationsSorter.METHOD)
                .defaultModelRendering(ModelRendering.MODEL)
                .build();
    }


    @Controller
    class SwaggerWelcome {
        SwaggerWelcome() {
        }

        @GetMapping({"/"})
        public String redirectToUi() {
            return "redirect:/swagger-ui/index.html";
        }

        @GetMapping({"/swagger-ui.html"})
        public String redirectOldUrl() {
            return "redirect:/swagger-ui/index.html";
        }



    }
}