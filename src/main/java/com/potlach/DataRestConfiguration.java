package com.potlach;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.potlach.util.ResourcesMapper;

@Configuration
public class DataRestConfiguration extends RepositoryRestMvcConfiguration{
		// We are overriding the bean that RepositoryRestMvcConfiguration
		// is using to convert our objects into JSON so that we can control
		// the format. The Spring dependency injection will inject our instance
		// of ObjectMapper in all of the spring data rest classes that rely
		// on the ObjectMapper. This is an example of how Spring dependency
		// injection allows us to easily configure dependencies in code that
		// we don't have easy control over otherwise.
		@Override
		public ObjectMapper halObjectMapper() {
			ResourcesMapper mapper = new ResourcesMapper();
			mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			return mapper;
		}
}
