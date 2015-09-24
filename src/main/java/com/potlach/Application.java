package com.potlach;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultiPartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.potlach.auth.OAuth2SecurityConfiguration;
import com.potlach.model.User;
import com.potlach.util.AuditorAwareImpl;

@EnableAutoConfiguration
@Configuration
@ComponentScan
@EnableWebMvc
@EnableJpaRepositories
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
@Import(value = { OAuth2SecurityConfiguration.class,
		DataRestConfiguration.class })
public class Application {

	private static final String MAX_REQUEST_SIZE = "15MB";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public AuditorAware<User> auditorProvider() {
		return new AuditorAwareImpl();
	}

	
	// This configuration element adds the ability to accept multipart
		// requests to the web container.
		@Bean
	    public MultipartConfigElement multipartConfigElement() {
			// Setup the application container to be accept multipart requests
			final MultiPartConfigFactory factory = new MultiPartConfigFactory();
			// Place upper bounds on the size of the requests to ensure that
			// clients don't abuse the web container by sending huge requests
			factory.setMaxFileSize(MAX_REQUEST_SIZE);
			factory.setMaxRequestSize(MAX_REQUEST_SIZE);

			// Return the configuration to setup multipart in the container
			return factory.createMultipartConfig();
		}
}
