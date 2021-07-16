package com.chilborne.todoapi.web.config;

import com.chilborne.todoapi.persistance.model.Task;
import com.chilborne.todoapi.persistance.model.ToDoList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.*;

@Configuration
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        Class[] ignoredClasses = {ToDoList.class, Task.class};

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/v1/**"))
                .build()
                .apiInfo(apiInfo())
                .produces(new HashSet<>(List.of("application/json")))
                .consumes(new HashSet<>(List.of("application/json")))
                .ignoredParameterTypes(ignoredClasses);
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "To Do List Api",
                "A simple REST api to make and manage To Do Lists",
                "0.0.1",
                "TOS",
                new Contact("Chris Hilborne","https://github.com/ChrisHilborne", "chris.hilborne@gmail.com"),
                "Apache 2.0",
                "https://github.com/ChrisHilborne/to-do-api/blob/main/LICENSE",
                new ArrayList<VendorExtension>()
                );
    }
}

