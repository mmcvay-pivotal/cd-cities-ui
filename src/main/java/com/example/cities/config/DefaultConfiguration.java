package com.example.cities.config;

import com.example.cities.client.repositories.CityRepository;
import com.example.cities.client.repositories.CityRepositoryFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("default")
public class DefaultConfiguration {
    @Bean
    public CityRepository cityRepository() {
        return new CityRepositoryFactory().create("http://cd-cities-service.cfapps.io/");
    }
}
