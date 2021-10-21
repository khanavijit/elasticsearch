package com.avijit.projects.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.avijit.projects.elasticsearch")
public class ElasticsearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElasticsearchApplication.class, args);
	}

}
