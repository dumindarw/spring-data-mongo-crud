package com.drw.mongoex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

@SpringBootApplication
public class MongoExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MongoExampleApplication.class, args);
	}

	@Bean
	public Jackson2RepositoryPopulatorFactoryBean getRespositoryPopulator(){
		Jackson2RepositoryPopulatorFactoryBean factoryBean = new Jackson2RepositoryPopulatorFactoryBean();
		factoryBean.setResources(new Resource[]{new ClassPathResource("products.json")});
		return factoryBean;
	}

}
