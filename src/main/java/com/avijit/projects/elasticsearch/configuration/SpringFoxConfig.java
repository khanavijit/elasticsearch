package com.avijit.projects.elasticsearch.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class SpringFoxConfig {


    public static final String NAME = "Avijit Khan";
    public static final String EMAIL = "khanavijit@gmail.com";
    public static final String URL = "";
    public static final Contact DEFAULT_CONTACT = new Contact(NAME, URL, EMAIL);
    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES =
            new HashSet<String>(Arrays.asList("application/json"));


    @Bean
    public Docket api() {
        ApiInfo apiInfo = (new ApiInfoBuilder())
                .title("User Suport Tickets Search")
                .description("An API that can search user support tickets")
                .version("1.0.0")
                .contact(DEFAULT_CONTACT)
                .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.avijit.projects"))
                .paths(PathSelectors.any())
                .build()
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES)
                .apiInfo(apiInfo);
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