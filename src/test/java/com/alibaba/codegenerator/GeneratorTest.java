package com.alibaba.codegenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = {"com.alibaba.codegenerator"}, excludeFilters={
		  @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value=Generator.class)})
public class GeneratorTest {

	public static void main(String[] args) {
		SpringApplication.run(GeneratorTest.class, args);
	}
}
