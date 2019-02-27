package com.example.restdemo.configuration;

import com.example.restdemo.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionsConfig {

    @Bean
    @Qualifier("taskNotFoundException")
    public ResourceNotFoundException getResourceNotFoundException() {

        return new ResourceNotFoundException("Task not Found");
    }

}
